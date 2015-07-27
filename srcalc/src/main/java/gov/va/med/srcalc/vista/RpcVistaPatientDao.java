package gov.va.med.srcalc.vista;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.dao.RecoverableDataAccessException;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import gov.va.med.crypto.VistaKernelHash;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.calculation.RetrievedValue;

/**
 * Implementation of {@link VistaPatientDao} using remote procedures. Each
 * instance is tied to a particular user to avoid having to specify the user
 * when calling each method.
 */
public class RpcVistaPatientDao implements VistaPatientDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcVistaPatientDao.class);
    private static final String NO_WEIGHT = "0^NO WEIGHT ENTERED WITHIN THIS PERIOD";
    private static final String VITALS_SPLIT_REGEX = "[\\s]+";
    private static final String WEIGHT_UNITS = "lbs.";
    private static final String HEIGHT_UNITS = "inches";
    
    public static final String VISTA_DATE_OUTPUT_FORMAT = "MM/dd/yy@HH:mm";
    
    private static final Map<String, String> TRANSLATION_MAP;
    
    /**
     * Using a map instead of a different type of collection for efficiency when
     * filtering for health factors.
     */
    private static final Map<String, Boolean> VALID_HEALTH_FACTORS;

    /**
     * Static class initializer to fill the translation map with the proper values.
     */
    static {
        final Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("M", "Male");
        tempMap.put("F", "Female");
        TRANSLATION_MAP = Collections.unmodifiableMap(tempMap);
        final Map<String, Boolean> tempHealthFactorsMap = new HashMap<String,Boolean>();
        for(int i = 0; i < HEALTH_FACTORS_ARRAY.length; i++)
        {
            tempHealthFactorsMap.put(HEALTH_FACTORS_ARRAY[i], true);
        }
        VALID_HEALTH_FACTORS = ImmutableMap.copyOf(tempHealthFactorsMap);
    }
    
    private final VistaProcedureCaller fProcedureCaller;
    
    private final String fDuz;
    
    /**
     * Constructs an instance.
     * @param procedureCaller for making the procedure calls
     * @param duz the user DUZ under which to perform the procedure calls
     */
    public RpcVistaPatientDao(
            final VistaProcedureCaller procedureCaller, final String duz)
    {
        fProcedureCaller = procedureCaller;
        fDuz = duz;
    }
    
    /**
     * {@inheritDoc}
     * <p>This method will eager load all available information about the patient including vitals,
     * basic information, and lab results. Eager loading is done to sacrifice efficiency for
     * simplicity. The number of eager remote procedure calls is comparable to VistA CPRS.</p>
     * @param dfn the dfn identifying the specified patient
     */
    @Override
    public Patient getPatient(final int dfn)
    {
        final List<String> basicResults;
        basicResults = fProcedureCaller.doRpc(fDuz, RemoteProcedure.GET_PATIENT, String.valueOf(dfn));
        final List<String> vitalResults;
        vitalResults = fProcedureCaller.doRpc(fDuz, RemoteProcedure.GET_RECENT_VITALS, String.valueOf(dfn));
        try
        {
            // Fields are separated by '^'
            // Basic patient demographics (age, gender)
            final List<String> basicArray = Splitter.on('^').splitToList(basicResults.get(0));
            final String patientName = basicArray.get(0);
            final int patientAge = Integer.parseInt(basicArray.get(1));
            final String patientGender = translateFromVista(basicArray.get(2));
            final Patient patient = new Patient(dfn, patientName, patientGender, patientAge);
            // Patient vitals information (including but not limited to BMI, height, weight, weight 6 months ago)
            // If there are no results, a single line with an error message is returned.
            LOGGER.debug("Patient Vital Results: {}", vitalResults);
            if (vitalResults.size() > 1)
            {
                // Parse the returned data and put it into the patient data
                // This will include the most recent height, current weight, and BMI
                parseRecentVitalResults(patient, vitalResults);
            }
            
            // We have to get the current weight before we do this
            // If there was no current weight, no need to retrieve other weight
            if (patient.getWeight() != null)
            {
                final List<String> weightResults = retrieveWeight6MonthsAgo(patient);
                LOGGER.debug("Weight Results: {}", weightResults);
                // A line begging with "0^NO" means that no results were retrieved
                // The actual line varies depending on the vital requested.
                if (weightResults.size() > 0 && !weightResults.get(0).equals(NO_WEIGHT))
                {
                    LOGGER.debug("Patient Vital Results: {}", weightResults);
                    // Parse the returned data and put it into the patient data
                    // This includes weight and BMI currently.
                    parseWeightResults(patient, weightResults);
                }
            }
            
            // Retrieve all labs from VistA
            retrieveLabs(dfn, patient);
            
            // Retrieve all health factors in the last year from VistA and filter
            // by the list given to us by the NSO.
            retrieveHealthFactors(dfn, patient);
            
            LOGGER.debug("Loaded {} from VistA.", patient);
            return patient;
        }
        catch (final Exception e)
        {
            // There are many DataAccessExcpeionts, but this seems like
            // the most appropriate exception to throw here.
            throw new NonTransientDataAccessResourceException(e.getMessage(), e);
        }
    }

    private static String translateFromVista(final String vistaField)
    {
        if(TRANSLATION_MAP.containsKey(vistaField))
        {
            return TRANSLATION_MAP.get(vistaField);
        }
        return "Unknown";
    }
    
    private List<String> retrieveWeight6MonthsAgo(final Patient patient)
    {
        // Our range for weight 6 months ago is 3-12 months prior to the
        // most recent weight.
        final Calendar cal = Calendar.getInstance();
        cal.setTime(patient.getWeight().getMeasureDate());
        cal.add(Calendar.MONTH, -6);
        final String endDateString = String.format("%03d%02d%02d", (cal.get(Calendar.YEAR) - 1700),
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        cal.setTime(patient.getWeight().getMeasureDate());
        cal.add(Calendar.YEAR, -1);
        final String startDateString = String.format("%03d%02d%02d", (cal.get(Calendar.YEAR) - 1700),
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        final String rpcParameter = String.valueOf(patient.getDfn()) + "^" + endDateString + "^WT^" + startDateString;
        LOGGER.debug("Weight 6 Months Ago Parameter: {}", rpcParameter);
        return fProcedureCaller.doRpc(fDuz, RemoteProcedure.GET_VITAL, rpcParameter);
    }
    
    private void parseWeightResults(final Patient patient, final List<String> weightResults) throws ParseException
    {
        /*-
         * The last entries are the most recent so we use those. Get the most recent weight measurement within the
         * already specified range. The format expected from VistA is:
         * 21557^04/17/09@12:00 Wt: 185.00 lb (84.09 kg) _NURSE,ONE
         *        @12:00 Body Mass Index: 25.86
         * 22296^08/24/09@14:00 Wt: 190.00 lb (86.36 kg) _NURSE,ONE
         *        @14:00 Body Mass Index: 26.56
         * Where the most recent weight is the last result and each result consists of two
         * lines. The first line is a measurement identifier, the date and time, the weight in pounds and kilograms and
         * the person providing the measurement. The second line is the time on the same date as the weight measurement,
         * along with the BMI for the patient.
         */
        final List<String> weightLineTokens = Splitter.on(Pattern.compile("[\\s\\^]+"))
                .splitToList(weightResults.get(weightResults.size()-2));
        // Get the date of the measurement
        final SimpleDateFormat dateFormat = new SimpleDateFormat(VISTA_DATE_OUTPUT_FORMAT);
        LOGGER.debug("weight line tokens: {}", weightResults);
        final Date measurementDate = dateFormat.parse(weightLineTokens.get(1));
        patient.setWeight6MonthsAgo(new RetrievedValue(
                Double.parseDouble(weightLineTokens.get(3)), measurementDate, WEIGHT_UNITS));
        LOGGER.debug("Weight 6 months ago: {}", patient.getWeight6MonthsAgo());
    }
    
    private void parseRecentVitalResults(final Patient patient, final List<String> vitalResults) throws ParseException
    {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("(" + VISTA_DATE_OUTPUT_FORMAT + ")");
        final Pattern compliedPattern = Pattern.compile(VITALS_SPLIT_REGEX);
        // Each entry comes with an accompanying date and time.
        final List<String> heightLineTokens = Splitter.on(compliedPattern).splitToList(vitalResults.get(5));
        final int feet = Integer.parseInt(heightLineTokens.get(2));
        patient.setHeight(new RetrievedValue(
                (feet * 12.0) + Double.parseDouble(heightLineTokens.get(4)),
                dateFormat.parse(heightLineTokens.get(1)),
                HEIGHT_UNITS));
        final List<String> weightLineTokens = Splitter.on(compliedPattern).splitToList(vitalResults.get(6));
        patient.setWeight(new RetrievedValue(
                Double.parseDouble(weightLineTokens.get(2)),
                dateFormat.parse(weightLineTokens.get(1)),
                WEIGHT_UNITS));
        final List<String> bmiLineTokens = Splitter.on(compliedPattern).splitToList(vitalResults.get(7));
        // The BMI value is the second to last token on its line
        patient.setBmi(new RetrievedValue(
            Double.parseDouble(bmiLineTokens.get(bmiLineTokens.size()-2)),
            patient.getWeight().getMeasureDate(),
            ""));
    }
    
    private void retrieveLabs(final int dfn, final Patient patient) throws ParseException
    {
        final VistaLabs[] enumValues = VistaLabs.values();
        for(final VistaLabs labRetrievalEnum: enumValues)
        {
            try
            {
                final String rpcResultString = fProcedureCaller.doRetrieveLabs(
                        fDuz,
                        String.valueOf(dfn),
                        labRetrievalEnum.getPossibleLabNames());
                // If the resultString is a success, add it to the patient's lab data.
                // Else, we don't need to do anything.
                if(!rpcResultString.isEmpty())
                {
                    List<String> rpcSplit = Splitter.on('^').splitToList(rpcResultString);
                    final double labValue = Double.parseDouble(rpcSplit.get(1));
                    final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy@HH:mm:ss");
                    patient.getLabs().put(labRetrievalEnum.name(),
                            new RetrievedValue(labValue, format.parse(rpcSplit.get(2)), rpcSplit.get(3)));
                }
            }
            catch(final Exception e)
            {
                // If an exception occurs for any reason, move to the next lab so that as much patient
                // data as possible can still be retrieved.
                LOGGER.warn("Unable to retrieve lab {}. {}", labRetrievalEnum.name(), e.toString());
            }
        }
    }
    
    private void retrieveHealthFactors(final int dfn, final Patient patient)
    {
        try
        {
            patient.getHealthFactors().clear();
            final List<String> rpcResults = fProcedureCaller.doRpc(
                    fDuz, RemoteProcedure.GET_HEALTH_FACTORS, String.valueOf(dfn));
            // Now that we have all of the health factors, filter out any that are not present
            // in the list provided by the NSO.
            final Iterator<String> iter = rpcResults.iterator();
            while(iter.hasNext())
            {
                final String healthFactor = iter.next();
                final String[] factorSplitArray = healthFactor.split("\\^");
                if(VALID_HEALTH_FACTORS.get(factorSplitArray[1]) != null)
                {
                    final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
                    patient.getHealthFactors().add(new HealthFactor(format.parse(factorSplitArray[0]), factorSplitArray[1]));
                }
            }
            LOGGER.debug("Retrieved Health factors: {} ", patient.getHealthFactors());
        }
        catch(final Exception e)
        {
            LOGGER.warn("Unable to retrieve health factors. {}", e.toString());
        }
    }
    
    @Override
    public SaveNoteCode saveRiskCalculationNote(final int patientDfn, final String electronicSignature,
            final String noteBody)
    {
        // Split on line feed or carriage return
        // Wrap any lines that are too long so that users do not have to
        // scroll when viewing the note in CPRS.
        final List<String> bodyArray = Splitter.on(Pattern.compile("\\r?\\n")).splitToList(noteBody);
        final StringBuilder wrappedNote = new StringBuilder();
        for (final String line : bodyArray)
        {
            wrappedNote.append(WordUtils.wrap(line, VistaPatientDao.MAX_LINE_LENGTH, "\n    ", false) + "\n");
        }
        try
        {
            final String rpcResultString = fProcedureCaller.doSaveProgressNoteCall(fDuz,
                    VistaKernelHash.encrypt(electronicSignature, false), String.valueOf(patientDfn),
                    // Use Guava Splitter to get a List.
                    Splitter.on('\n').splitToList(wrappedNote));
            
            final VistaOperationResult rpcResult = VistaOperationResult.fromString(rpcResultString);
            if (rpcResult.getCode().equals("1"))
            {
                return SaveNoteCode.SUCCESS;
            }
            else
            {
                return SaveNoteCode.INVALID_SIGNATURE;
            }
        }
        catch (final Exception e)
        {
            // An Exception means an invalid DUZ or a problem with VistALink/VistA
            // Translate the exception into a status message
            // We've tested that this works so any failure at this point is probably recoverable.
            throw new RecoverableDataAccessException(e.getMessage(), e);
        }
    }
}

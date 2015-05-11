package gov.va.med.srcalc.vista;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.dao.RecoverableDataAccessException;

import gov.va.med.crypto.VistaKernelHash;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.vistalink.rpc.RpcRequest;

/**
 * Implementation of {@link VistaPatientDao} using remote procedures. Each
 * instance is tied to a particular user to avoid having to specify the user
 * when calling each method.
 */
public class RpcVistaPatientDao implements VistaPatientDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(RpcVistaPatientDao.class);
    private static final String NO_WEIGHT = "0^NO WEIGHT ENTERED WITHIN THIS PERIOD";
    private static final String SPLIT_REGEX = "[\\s]+";
    
    public static final String VISTA_DATE_OUTPUT_FORMAT = "MM/dd/yy@HH:mm";
    
    private static final Map<String, String> TRANSLATION_MAP;
    /**
     * Static class initializer to fill the translation map with the proper values.
     */
	static {
		final Map<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("M", "Male");
		tempMap.put("F", "Female");
		TRANSLATION_MAP = Collections.unmodifiableMap(tempMap);
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
    
    @Override
    public Patient getPatient(final int dfn)
    {
        final List<String> basicResults;
        	basicResults = fProcedureCaller.doRpc(
                fDuz, RemoteProcedure.GET_PATIENT, String.valueOf(dfn));
        final List<String> vitalResults;
        	vitalResults = fProcedureCaller.doRpc(
        			fDuz, RemoteProcedure.GET_RECENT_VITALS, String.valueOf(dfn));
    	try
    	{
	        // Fields are separated by '^'
    		// Basic patient demographics (age, gender)
	        final String[] basicArray = basicResults.get(0).split("\\^");
	        final String patientName = basicArray[0];
	        final int patientAge = Integer.parseInt(basicArray[1]);
	        final String patientGender = translateFromVista(basicArray[2]);
	        final Patient patient = new Patient(dfn, patientName, patientGender, patientAge);
	        // Patient vitals information (including but not limited to BMI, height, weight, weight 6 months ago)
	        // If there are no results, a single line with an error message is returned.
	        fLogger.debug("Patient Vital Results: {}" ,vitalResults);
	        if(vitalResults.size() > 1)
	        {
	        	// Parse the returned data and put it into the patient data
	        	// This will include the most recent height, current weight, and BMI
	        	parseRecentVitalResults(patient, vitalResults);
	        }
	        
	        // We have to get the current weight before we do this
	        // If there was no current weight, no need to retrieve other weight
	        if(patient.getWeightDate() != null)
	        {
	        	final List<String> weightResults = retrieveWeight6MonthsAgo(patient);
	        	fLogger.debug("Weight Results: {}", weightResults);
	        	// A line begging with "0^NO" means that no results were retrieved
	        	// The actual line varies depending on the vital requested.
		        if(weightResults.size() > 0 && !weightResults.get(0).equals(NO_WEIGHT))
	        	{
		        	fLogger.debug("Patient Vital Results: {}",weightResults);
		        	// Parse the returned data and put it into the patient data
		        	// This includes weight and BMI currently.
		        	parseWeightResults(patient, weightResults);
	        	}
	        }
	        fLogger.debug("Loaded {} from VistA.", patient);
	        return patient;
    	}
    	catch(final Exception e)
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
        cal.setTime(patient.getWeightDate());
        cal.add(Calendar.MONTH, -6);
        final String endDateString = String.format("%03d%02d%02d", (cal.get(Calendar.YEAR) - 1700),
        		cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        cal.setTime(patient.getWeightDate());
        cal.add(Calendar.YEAR, -1);
        final String startDateString = String.format("%03d%02d%02d", (cal.get(Calendar.YEAR) - 1700),
        		cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        final String rpcParameter = String.valueOf(patient.getDfn()) + "^" + endDateString + "^WT^" + startDateString;
        fLogger.debug("Weight 6 Months Ago Parameter: {}", rpcParameter);
    	return fProcedureCaller.doRpc(fDuz, RemoteProcedure.GET_VITAL, rpcParameter);
    }
    
    private void parseWeightResults(final Patient patient, final List<String> weightResults) throws ParseException
    {
    	// The last entries are the most recent so we use those.
    	// Get the most recent weight measurement within the already specified range.
    	final String[] weightLineTokens = weightResults.get(weightResults.size()-2).split("[\\s\\^]+");
    	patient.setWeight6MonthsAgo(Double.parseDouble(weightLineTokens[3]));
    	// Get the date of the measurement
    	final SimpleDateFormat dateFormat = new SimpleDateFormat(VISTA_DATE_OUTPUT_FORMAT);
    	final Date measurementDate = dateFormat.parse(weightLineTokens[1]);
    	patient.setWeight6MonthsAgoDate(measurementDate);
    	fLogger.debug("Weight 6 months ago: {}", patient.getWeight6MonthsAgo());
    }
    
    private void parseRecentVitalResults(final Patient patient, final List<String> vitalResults) throws ParseException
    {
    	final SimpleDateFormat dateFormat = new SimpleDateFormat("(" + VISTA_DATE_OUTPUT_FORMAT + ")");
    	// Each entry comes with an accompanying date and time.
    	final String[] heightLineTokens = vitalResults.get(5).split(SPLIT_REGEX);
    	final int feet = Integer.parseInt(heightLineTokens[2]);
    	patient.setHeight((feet * 12) + Integer.parseInt(heightLineTokens[4]));
    	patient.setHeightDate(dateFormat.parse(heightLineTokens[1]));
    	final String[] weightLineTokens = vitalResults.get(6).split(SPLIT_REGEX);
    	patient.setWeight(Double.parseDouble(weightLineTokens[2]));
    	patient.setWeightDate(dateFormat.parse(weightLineTokens[1]));
    	final String[] bmiLineTokens = vitalResults.get(7).split(SPLIT_REGEX);
    	// The BMI value is the second to last token on its line
    	patient.setBmi(Double.parseDouble(bmiLineTokens[bmiLineTokens.length-2]));
    	patient.setBmiDate(patient.getWeightDate());
    }

	@Override
	public SaveNoteCode saveRiskCalculationNote(final int patientDfn,
			final String electronicSignature, final String noteBody)
	{
		// Split on line feed or carriage return
		// Wrap any lines that are too long so that users do not have to 
		// scroll when viewing the note in CPRS.
		final String[] bodyArray = noteBody.split("\\r?\\n");
		final StringBuilder wrappedNote = new StringBuilder();
		for(final String line: bodyArray)
		{
			wrappedNote.append(WordUtils.wrap(line, VistaPatientDao.MAX_LINE_LENGTH, "\n    ", false) + "\n");
		}
		final String[] wrappedBodyArray = wrappedNote.toString().split("\\n");
		final Map<String, String> noteMap = new HashMap<String, String>();
		for(int i = 0; i < wrappedBodyArray.length; i++)
		{
			// VistA indexing starts at 1
			// The current RPC uses multiple subscripts
			noteMap.put(RpcRequest.buildMultipleMSubscriptKey(String.format("\"TEXT\",%d,0",i + 1)),
					wrappedBodyArray[i]);
		}
		try
		{
			final List<String> saveResults;
			saveResults = fProcedureCaller.doRpc(
	            fDuz, RemoteProcedure.SAVE_PROGRESS_NOTE,
	            fDuz, VistaKernelHash.encrypt(electronicSignature, false), String.valueOf(patientDfn), noteMap);
			final String[] splitArray = saveResults.get(0).split("\\^");
			if(splitArray[0].equals("1"))
			{
				return SaveNoteCode.SUCCESS;
			}
			else
			{
				return SaveNoteCode.INVALID_SIGNATURE;
			}
		}
		catch(final Exception e)
		{
			// An Exception means an invalid DUZ or a problem with VistALink/VistA
			// Translate the exception into a status message
			// We've tested that this works so any failure at this point is probably recoverable.
			throw new RecoverableDataAccessException(e.getMessage(), e);
		}
	}
}

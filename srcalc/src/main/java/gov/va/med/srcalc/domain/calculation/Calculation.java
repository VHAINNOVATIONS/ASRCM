package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.model.*;

import java.io.Serializable;
import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Ints;

/**
 * <p>A risk calculation. You can think of this as a step-by-step builder of
 * {@link CalculationResult} objects: set the patient, specialty, etc., and then
 * call {@link #calculate(Collection, VistaPerson)} to produce the results.</p>
 * 
 * <p>A stateful builder such as this (as opposed to a one-shot <code>calculate()</code>
 * which could take all of the necessary data) allows features such as tracking
 * the time the user takes to run a calculation and rerunning a calculation with
 * tweaked input values.</p>
 */
public class Calculation implements Serializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Calculation.class);

    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    private final DateTime fStartDateTime;
    private Patient fPatient;
    private Specialty fSpecialty;
    private Optional<HistoricalCalculation> fHistoricalCalculation;
    
    /**
     * This class presents a pure JavaBean interface, with a default constructor and
     * mutators for fields. It also offers convenience factory methods below.
     */
    public Calculation()
    {
        fStartDateTime = DateTime.now();
        fHistoricalCalculation = Optional.absent();
    }
    
    /**
     * Returns a new calculation for the specified patient.
     * @param patient the patient to use for the new calculation.
     */
    public static Calculation forPatient(final Patient patient)
    {
        final Calculation c = new Calculation();
        c.setPatient(patient);
        return c;
    }
    
    /**
     * The DateTime at which this builder object was created. Useful for
     * tracking the amount of time it takes a user to run a calculation.
     */
    public DateTime getStartDateTime()
    {
        return fStartDateTime;
    }

    /**
     * Returns the patient to which the calculation belongs.
     */
    public Patient getPatient()
    {
        return fPatient;
    }

    /**
     * Set the patient for this calculation.
     * @param patient
     */
    public void setPatient(final Patient patient)
    {
        this.fPatient = patient;
    }

    /**
     * Returns the specialty being used for this calculation.
     */
    public Specialty getSpecialty()
    {
        return fSpecialty;
    }

    /**
     * Set the specialty for this calculation.
     */
    public void setSpecialty(final Specialty specialty)
    {
        this.fSpecialty = specialty;
    }
    
    /**
     * <p>Returns the Set of {@link Variable}s that the calculation requires.</p>
     * 
     * <p>The set will never contain more than one {@link ProcedureVariable}.</p>
     * @throws IllegalStateException if no specialty has been set.
     * @throws ConfigurationException if the set specialty requires multiple
     * ProcedureVariables. (The admin UI does not allow definition of multiple
     * ProcedureVariables.)
     * @return an ImmutableSet
     */
    public ImmutableSet<Variable> getVariables()
    {
        // Ensure we are in the proper state.
        if (fSpecialty == null)
        {
            throw new IllegalStateException(
                    "Cannot return list of variables because no specialty has been set.");
        }
        
        final ImmutableSet<Variable> vars = fSpecialty.getModelVariables();
        // Check our method contract.
        if (hasMultipleProcedureVariables(vars))
        {
            // See method contract.
            throw new ConfigurationException(
                    "The specialty requires multiple ProcedureVariables, which is unsupported.");
        }
        return vars;
    }
    
    /**
     * Returns true if the given collection of variables contains more than 1
     * instance of {@link ProcedureVariable}. False otherwise.
     */
    private static boolean hasMultipleProcedureVariables(
            final Collection<Variable> variables)
    {
        boolean foundProcedure = false;
        for (final Variable var : variables)
        {
            if (var instanceof ProcedureVariable)
            {
                if (foundProcedure)
                {
                    return true;
                }
                else
                {
                    foundProcedure = true;
                }
            }
        }
        return false;
    }

    /**
     * Runs the calculation for each outcome with the given Values.
     * @param values the variable values as inputs to the calculation
     * @param user the user running the calculation
     * @return the results as a CalculationResult object
     * @throws IllegalArgumentException if incomplete values are provided
     * @throws MissingValuesException if there are any required variables without an assigned value
     */
    public CalculationResult calculate(
            final Collection<Value> values, final VistaPerson user)
            throws MissingValuesException
    {
        // Run the calculation first to make sure we don't get any exceptions.
        final TreeMap<String, Float> outcomes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        // Collect all missing variables into this set.
        final Set<Variable> missingVars = new HashSet<>();
        for (final RiskModel model : getSpecialty().getRiskModels())
        {
            try
            {
                outcomes.put(model.getDisplayName(), model.calculate(values));
            }
            catch (final MissingValuesException e)
            {
                missingVars.addAll(e.getMissingVariables());
            }
        }

        if (!missingVars.isEmpty())
        {
            LOGGER.debug("Could not run calculation due to missing values: {}", missingVars);
            throw new MissingValuesException(missingVars);
        }
        
        final DateTime resultTime = DateTime.now();
        
        // If this was the first run, record the HistoricalCalculation for
        // getHistoricalCalculation().
        if (!fHistoricalCalculation.isPresent())
        {
            final HistoricalCalculation hc = makeHistoricalCalculation(resultTime, user);
            fHistoricalCalculation = Optional.of(hc);
            LOGGER.debug("Captured historical information: {}", hc);
        }
        
        final CalculationResult result = new CalculationResult(
                // This is guaranteed to be set now.
                fHistoricalCalculation.get(),
                fPatient.getDfn(),
                resultTime,
                // The constructor requires a Set, not just a Collection.
                ImmutableSet.copyOf(values),
                outcomes);
        
        return result;
    }
    
    /**
     * Constructs a HistoricalCalculation object from this Calculation, given the
     * timestamp of the first result.
     * @param firstResultTime the timestamp of the first result, to derive
     * secondsToFirstResult
     * @param user the user running the calculation
     * @return a new HistoricalCalculation
     * @throws NullPointerException if any argument is null
     */
    private HistoricalCalculation makeHistoricalCalculation(
            final DateTime firstResultTime, final VistaPerson user)
    {
        Objects.requireNonNull(firstResultTime);
        Objects.requireNonNull(user);

        final Duration durationToResult = new Duration(fStartDateTime, firstResultTime);
        // It is extremely unlikely that this number of seconds will exceed
        // Integer.MAX_VALUE, but this checked cast will throw an IllegalArgumentException
        // if so.
        final int secondsToResult = Ints.checkedCast(durationToResult.getStandardSeconds());
        return new HistoricalCalculation(
                fSpecialty.getName(),
                user.getStationNumber(),
                fStartDateTime,
                secondsToResult,
                user.getProviderType());
    }
    
    /**
     * If {@link #calculate(Collection, VistaPerson)} has been called, returns the {@link
     * HistoricalCalculation} object encapsulating historical data about this calculation.
     * @return the HistoricalCalculation (same instance every time), if it exists
     */
    public Optional<HistoricalCalculation> getHistoricalCalculation()
    {
        return fHistoricalCalculation;
    }
}

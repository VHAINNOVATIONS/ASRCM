package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.util.MissingValuesException;

import java.io.Serializable;
import java.util.*;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * <p>A risk calculation. You can think of this as a step-by-step builder of
 * {@link CalculationResult} objects: set the patient, specialty, etc., and then
 * call {@link #calculate(Collection)} to produce the results.</p>
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

    private DateTime fStartDateTime;
    private Patient fPatient;
    private Specialty fSpecialty;
    
    /**
     * This class presents a pure JavaBean interface, with a default constructor
     * and mutators for all fields. It also offers convenience factory methods
     * below.
     */
    public Calculation()
    {
        fStartDateTime = new DateTime();
    }
    
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

    public Patient getPatient()
    {
        return fPatient;
    }

    public void setPatient(final Patient patient)
    {
        this.fPatient = patient;
    }

    public Specialty getSpecialty()
    {
        return fSpecialty;
    }

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
     * @throws IllegalArgumentException if incomplete values are provided
     * @return the outcomes for convenience
     * @throws MissingValuesException if there are any required variables without an assigned value
     */
    public CalculationResult calculate(final Collection<Value> values) throws MissingValuesException
    {
        // Run the calculation first to make sure we don't get any exceptions.
        final TreeMap<String, Float> outcomes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        // TODO: Change into a Set.
        final List<MissingValueException> missingValues = new ArrayList<MissingValueException>();
        for (final RiskModel model : getSpecialty().getRiskModels())
        {
            try
            {
                outcomes.put(model.getDisplayName(), model.calculate(values));
            }
            catch(MissingValuesException e)
            {
                missingValues.addAll(e.getMissingValues());
            }
        }

        if(missingValues.size() > 0)
        {
            LOGGER.debug("Could not run calculation due to missing values: {}", missingValues);
            throw new MissingValuesException("The calculation is missing values.", missingValues);
        }
        
        return new CalculationResult(
                fStartDateTime,
                fPatient.getDfn(),
                fSpecialty.getName(),
                // The constructor requires a Set, not just a Collection.
                ImmutableSet.copyOf(values),
                outcomes);
    }
}

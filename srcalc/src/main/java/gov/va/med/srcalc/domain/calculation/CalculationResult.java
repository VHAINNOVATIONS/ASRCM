package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.DisplayNameComparator;
import gov.va.med.srcalc.domain.model.Specialty;

import java.io.Serializable;
import java.util.*;

import org.joda.time.DateTime;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

/**
 * <p>A calculation result. Stores both the calculation input values and the
 * outcomes. Immutable as long as the provided {@link Value} objects are
 * immutable.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class CalculationResult implements Serializable
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    private final DateTime fStartDateTime;
    private final int fPatientDfn;
    private final String fSpecialtyName;
    private final ImmutableSortedSet<Value> fValues;
    private final ImmutableSortedMap<String, Double> fOutcomes;
    
    /**
     * <p>Constructs an instance with the given properties.</p>
     * 
     * <p>Note: does not perform any consistency check on the data. For example,
     * this constructor will accept outcomes for models not in the given
     * specialty.</p>
     * 
     * @param startDateTime when the calculation was started, for metrics
     * @param patientDfn the associated patient DFN
     * @param specialtyName the associated specialty name, must not be null
     * @param values the calculation input values, must not be null. The passed
     * collection may be mutable as this object will take an immutable snapshot.
     * @param outcomes the calculation outcomes, must not be null. The passed
     * collection may be mutable as this object will take an immutable snapshot.
     * @throws NullPointerException if any argument is null or any value in a
     * collection argument is null
     */
    public CalculationResult(
            final DateTime startDateTime,
            final int patientDfn,
            final String specialtyName,
            final Set<Value> values,
            final Map<String, Double> outcomes)
    {
        fStartDateTime = startDateTime;
        fPatientDfn = patientDfn;
        fSpecialtyName = Objects.requireNonNull(
                specialtyName, "specialty name must be non-null");
        fValues = ImmutableSortedSet.copyOf(
                new ValueVariableComparator(new DisplayNameComparator()), values);
        fOutcomes = ImmutableSortedMap.copyOf(
                outcomes, String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * The DateTime at which this Calculation first began (for example, when the
     * user first opened the tool).
     */
    public DateTime getStartDateTime()
    {
        return fStartDateTime;
    }

    /**
     * <p>Returns the DFN of the associated patient.</p>
     * 
     * <p>Although it would offer more flexilibty, this object does not store
     * a {@link Patient} object itself to preserve immutability.</p>
     */
    public int getPatientDfn()
    {
        return fPatientDfn;
    }

    /**
     * <p>Returns the name of the associated surgical specialty.</p>
     * 
     * <p>This object does not store the {@link Specialty} object itself to
     * preserve immutability.</p>
     */
    public String getSpecialtyName()
    {
        return fSpecialtyName;
    }
    
    /**
     * Returns the input values used to calculate the outcomes.
     */
    public ImmutableSortedSet<Value> getValues()
    {
        return fValues;
    }
    
    /**
     * Returns the risk outcomes as a Map from risk model name to calculated
     * risk.
     */
    public ImmutableSortedMap<String, Double> getOutcomes()
    {
        return fOutcomes;
    }
    
    /**
     * Returns a set of only {@link ProcedureValue} objects from the input values
     * of the calculation.
     * @return a SortedSet with only {@link ProcedureValue} objects in the set
     */
    public SortedSet<Value> getProcedureValues()
    {
    	final SortedSet<Value> procedureValues = new TreeSet<>(
    	        new ValueVariableComparator(new DisplayNameComparator()));
        for (final Value value : fValues)
        {
            if (value instanceof ProcedureValue)
            {
                procedureValues.add(value);
            }
        }
    	return procedureValues;
    }
    
    /**
     * Returns a set of any non-{@link ProcedureValue} objects from the input values
     * of the calculation.
     * @return a SortedSet with only non-{@link ProcedureValue} objects in the set
     */
    public SortedSet<Value> getNonProcedureValues()
    {
    	final SortedSet<Value> nonProcedureValues = new TreeSet<>(
    	        new ValueVariableComparator(new DisplayNameComparator()));
    	for(final Value value: fValues)
        {
            if (!(value instanceof ProcedureValue))
            {
                nonProcedureValues.add(value);
            }
        }
    	return nonProcedureValues;
    }
    
    /**
     * Builds a string version of the completed calculation.
     * @return a String with a human readable version of the completed calculation.
     */
    public String buildNoteBody()
    {
        final StringBuilder returnString = new StringBuilder();
        // Build the note body where each section is separated by a blank line

        // Specialty
        returnString.append(String.format("Specialty = %s%n%n", fSpecialtyName));
        // Procedures
        for (final Value value : this.getProcedureValues())
        {
            returnString.append(String.format(
                    "%s = %s%n",
                    value.getVariable().getDisplayName(), value.getDisplayString()));
        }
        // Model results
        returnString.append(String.format("%nResults%n"));
        for (final String key : this.getOutcomes().keySet())
        {
            returnString.append(String.format(
                    "%s = %.1f%%%n",
                    key, this.getOutcomes().get(key) * 100.0));
        }
        // Variable display names and values
        returnString.append(String.format("%nCalculation Inputs%n"));
        for (final Value value : this.getNonProcedureValues())
        {
            returnString.append(String.format(
                    "%s = %s%n",
                    value.getVariable().getDisplayName(), value.getDisplayString()));
        }
        return returnString.toString();
    }
}

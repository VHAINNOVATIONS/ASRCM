package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.DisplayNameComparator;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.domain.model.VariableGroup;

import java.io.Serializable;
import java.util.*;

import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.google.common.collect.*;

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
    private static final long serialVersionUID = 3L;
    
    private static final Comparator<Value> VALUE_COMPARATOR =
            new ValueVariableComparator(new DisplayNameComparator());

    private final HistoricalCalculation fHistoricalCalculation;
    private final DateTime fResultTime;
    private final int fPatientDfn;
    private final Optional<ProcedureValue> fProcedureValue;
    /**
     * The non-ProcedureValue values. Store it sorted by display name since we
     * need that order for {@link #buildNoteBody()}.
     */
    private final ImmutableSortedSet<Value> fNonProcedureValues;
    private final ImmutableSortedMap<String, Float> fOutcomes;
    
    /**
     * <p>Constructs an instance with the given properties.</p>
     * 
     * <p>Note: does not perform any consistency check on the data. For example,
     * this constructor will accept outcomes for models not in the given
     * specialty.</p>
     * 
     * @param historicalCalculation encapsulates calculation metadata
     * @param patientDfn the associated patient DFN
     * @param resultTime the time this result was calculated
     * @param values the calculation input values, must not be null. The passed
     * collection may be mutable as this object will take an immutable snapshot.
     * @param outcomes the calculation outcomes, must not be null. The passed
     * collection may be mutable as this object will take an immutable snapshot.
     * @throws NullPointerException if any argument is null or any value in a
     * collection argument is null
     * @throws IllegalArgumentException if the given values set contains multiple
     * ProcedureValues
     */
    public CalculationResult(
            final HistoricalCalculation historicalCalculation,
            final int patientDfn,
            final DateTime resultTime,
            final Set<Value> values,
            final Map<String, Float> outcomes)
    {
        fHistoricalCalculation = Objects.requireNonNull(historicalCalculation);
        fPatientDfn = patientDfn;
        fResultTime = Objects.requireNonNull(resultTime);

        // Split the values into Procedure and non-Procedure for storage. We do
        // this early to check the precondition.
        final ImmutableSortedSet.Builder<Value> otherValuesBuilder =
                ImmutableSortedSet.orderedBy(VALUE_COMPARATOR);
        Optional<ProcedureValue> procedureValue = Optional.absent();
        for (final Value value : values)
        {
            if (value instanceof ProcedureValue)
            {
                if (procedureValue.isPresent())
                {
                    throw new IllegalArgumentException(
                            "values contains multiple ProcedureValues");
                }
                else
                {
                    procedureValue = Optional.of((ProcedureValue)value);
                }
            }
            else
            {
                otherValuesBuilder.add(value);
            }
        }
        fProcedureValue = procedureValue;
        fNonProcedureValues = otherValuesBuilder.build();

        fOutcomes = ImmutableSortedMap.copyOf(
                outcomes, String.CASE_INSENSITIVE_ORDER);
    }
    
    /**
     * The DateTime at which this result was created.
     */
    public DateTime getResultTime()
    {
        return fResultTime;
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
        return fHistoricalCalculation.getSpecialtyName();
    }
    
    /**
     * Returns the input values used to calculate the outcomes.
     */
    public ImmutableSet<Value> getValues()
    {
        return ImmutableSet.<Value>builder()
                .addAll(fProcedureValue.asSet())  // handy!
                .addAll(fNonProcedureValues)
                .build();
    }
    
    /**
     * Returns the risk outcomes as a Map from risk model name to calculated
     * risk.
     */
    public ImmutableSortedMap<String, Float> getOutcomes()
    {
        return fOutcomes;
    }
    
    /**
     * Returns the ProcedureValue, which may not exist.
     * @return an Optional containing the ProcedureValue if it exists
     */
    public Optional<ProcedureValue> getProcedureValue()
    {
        return fProcedureValue;
    }

    /**
     * Extracts and returns the CPT code from the procedure value if present.
     * @return the CPT code, if there is a procedure value. Never null.
     */
    public Optional<String> getCptCode()
    {
        return fProcedureValue.isPresent() ?
                Optional.of(fProcedureValue.get().getValue().getCptCode()) :
                Optional.<String>absent();
    }
    
    /**
     * Returns a set of any non-{@link ProcedureValue} objects from the input values
     * of the calculation.
     * @return a SortedSet with only non-{@link ProcedureValue} objects in the set
     */
    public ImmutableSet<Value> getNonProcedureValues()
    {
        // Note: this is actually a SortedSet, but don't specify the sort order
        // in the contract to reduce coupling.
        return fNonProcedureValues;
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
        returnString.append(String.format("Specialty = %s%n%n", getSpecialtyName()));
        /* Procedure (if present)
         * We store the procedure value separately because the CPT code is needed
         * for storing discrete data in VistA. However, other procedure group values
         * need to be sorted before the other information.
         */
        if (fProcedureValue.isPresent())
        {
            final ProcedureValue value = fProcedureValue.get();
            returnString.append(String.format(
                    "%s = %s%n",
                    value.getVariable().getDisplayName(),
                    value.getDisplayString()));
        }
        final StringBuilder nonProcedureString = new StringBuilder();
        for (final Value value : fNonProcedureValues)
        {
            // Append the procedure group values now and the non procedure group values later.
            if(value.getVariable().getGroup().getName().equals(VariableGroup.PROCEDURE_GROUP))
            {
                returnString.append(String.format(
                        "%s = %s%n",
                        value.getVariable().getDisplayName(),
                        value.getDisplayString()));
            }
            else
            {
                // Build the string for use at the end of the results.
                nonProcedureString.append(String.format(
                    "%s = %s%n",
                    value.getVariable().getDisplayName(), value.getDisplayString()));
            }
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
        returnString.append(nonProcedureString);
        return returnString.toString();
    }
    
    /**
     * Returns a {@link SignedResult} with all the data from this object and a signature
     * time of now. Does not modify this object.
     * @return a new instance
     */
    public SignedResult signed()
    {
        // Convert the Value objects into the Map<String,String> representation.
        final HashMap<String, String> primitiveValues = new HashMap<>(getValues().size());
        for (final Value val : getValues())
        {
            primitiveValues.put(val.getVariable().getKey(), val.getValue().toString());
        }

        return new SignedResult(
                fHistoricalCalculation,
                fPatientDfn,
                getCptCode(),
                DateTime.now(), // see method doc
                primitiveValues,
                fOutcomes);
    }
}

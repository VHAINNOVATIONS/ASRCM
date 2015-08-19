package gov.va.med.srcalc.domain.model;

import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.calculation.Value;

/**
 * A ModelTerm that uses a single Variable.
 */
@MappedSuperclass  // why isn't this the default?
public abstract class SingleVariableTerm extends ModelTerm
{
    /**
     * Returns the associated {@link Variable}.
     * @return not null
     */
    @Transient // don't try to map this: subclasses will map it
    public abstract Variable getVariable();
    
    /**
     * For reflection-based construction only. Business code should use
     * {@link #SingleVariableTerm(float)}.
     */
    public SingleVariableTerm()
    {
    }
    
    /**
     * Constructs an instance.
     * @param coefficient
     */
    public SingleVariableTerm(final float coefficient)
    {
        super(coefficient);
    }
    
    @Override
    @Transient
    public ImmutableSet<Variable> getRequiredVariables()
    {
        return ImmutableSet.of(getVariable());
    }
    
    @Override
    public float getSummand(final Map<Variable, Value> inputValues) throws MissingValuesException
    {
        final Value value = inputValues.get(getVariable());
        if (value == null)
        {
            throw new MissingValuesException(ImmutableSet.of(getVariable()));
        }
        return getSummand(value);
    }
    
    /**
     * Returns the value to add to the risk model sum, given the value of the
     * single required variable.
     * @param inputValue must not be null
     * @throws IllegalArgumentException if the value is not of the required type
     */
    protected abstract float getSummand(final Value inputValue);
    
    /**
     * Base equals() functionality that verifies equality of the coefficient
     * and associated variable.
     * @param other must not be null
     * @return true if the coefficient and variables are equal, false otherwise
     */
    protected boolean baseEquals(final SingleVariableTerm other)
    {
        return super.baseEquals(other) && getVariable().equals(other.getVariable());
    }
}

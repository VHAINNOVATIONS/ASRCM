package gov.va.med.srcalc.domain.model;

import javax.persistence.Transient;

import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.util.CollectionUtils;
import gov.va.med.srcalc.util.NoNullSet;

/**
 * A ModelTerm that uses a single Variable.
 */
public abstract class SingleVariableTerm extends ModelTerm
{
    /**
     * Returns the associated {@link Variable}.
     * @return not null
     */
    public abstract Variable getVariable();
    
    /**
     * For reflection-based construction only. Business code should use
     * {@link #SingleVariableTerm(float)}.
     */
    public SingleVariableTerm()
    {
    }
    
    public SingleVariableTerm(final float coefficient)
    {
        super(coefficient);
    }
    
    @Override
    @Transient
    public NoNullSet<Variable> getRequiredVariables()
    {
        return NoNullSet.fromSet(CollectionUtils.unmodifiableSet(getVariable()));
    }
    
    /**
     * Base equals() functionality that verifies equality of the coefficient
     * and associated variable.
     * @return true if the coefficient and variables are equal, false otherwise
     */
    protected boolean baseEquals(final SingleVariableTerm other)
    {
        return super.baseEquals(other) && getVariable().equals(other.getVariable());
    }
}

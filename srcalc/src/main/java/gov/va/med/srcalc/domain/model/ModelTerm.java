package gov.va.med.srcalc.domain.model;

import javax.persistence.*;

import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.util.NoNullSet;

/**
 * A single summation term in a risk model.
 */
@MappedSuperclass
public abstract class ModelTerm
{
    private float fCoefficient = 0.0f;
    
    /**
     * Mainly intended for reflection-based construction. Business code should
     * use {@link #ModelTerm(float)}.
     */
    protected ModelTerm()
    {
    }
    
    /**
     * Constructs an instance.
     */
    public ModelTerm(final float coefficient)
    {
        fCoefficient = coefficient;
    }

    @Basic
    public float getCoefficient()
    {
        return fCoefficient;
    }

    public void setCoefficient(float coefficient)
    {
        fCoefficient = coefficient;
    }
    
    /**
     * Returns the required Variable(s) for the term. Unmodifiable and will not
     * contain nulls.
     */
    @Transient
    public abstract NoNullSet<Variable> getRequiredVariables();
    
    /**
     * Base equals() functionality that simply verifies equality of the coefficient.
     * @param other must not be null
     * @return true if the coefficients are equal, false otherwise
     */
    protected boolean baseEquals(final ModelTerm other)
    {
        return new Float(this.getCoefficient()).equals(other.getCoefficient());
    }
    
    // A reminder to subclasses to implement equals().
    @Override
    public abstract boolean equals(Object o);
    
    // A reminder to subclasses to implement hashCode().
    @Override
    public abstract int hashCode();
}

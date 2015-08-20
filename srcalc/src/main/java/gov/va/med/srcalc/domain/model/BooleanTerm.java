package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.Value;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 * <p>A {@link ModelTerm} for a {@link BooleanVariable}. Presents an immutable public
 * interface.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Embeddable
public final class BooleanTerm extends SingleVariableTerm
{
    private BooleanVariable fVariable;
    
    /**
     * For reflection-based construction only. Business code should use
     * {@link #BooleanTerm(BooleanVariable, float)}.
     */
    BooleanTerm()
    {
    }
    
    /**
     * Constructs an instance using the specified variable and its coefficient.
     * @see SingleVariableTerm#SingleVariableTerm(float)
     * @param variable
     * @param coefficient
     */
    public BooleanTerm(
            final BooleanVariable variable, final float coefficient)
    {
        super(coefficient);
        fVariable = Objects.requireNonNull(variable);
    }

    @ManyToOne(optional = false)
    public BooleanVariable getVariable()
    {
        return fVariable;
    }

    /**
     * For reflection-based construction only.
     * @deprecated because code should not explicitly call this method
     */
    @Deprecated
    void setVariable(BooleanVariable variable)
    {
        fVariable = Objects.requireNonNull(variable);
    }
    
    @Override
    public float getSummand(final Value inputValue)
    {
        final boolean isTrue = Boolean.TRUE.equals(inputValue.getValue());
        
        return isTrue ? getCoefficient() : 0.0f;
    }
    
    @Override
    public boolean equals(final Object o)
    {
        // Performance optimization.
        if (o == this)
        {
            return true;
        }
        
        if (o instanceof BooleanTerm)
        {
            return baseEquals((BooleanTerm)o);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getCoefficient(), getVariable());
    }
    
    @Override
    public String toString()
    {
        // The question mark indicates the boolean nature of the variable.
        return String.format("%s?=>%s", getVariable(), getCoefficient());
    }
    
    @Override
    public void accept(final ModelTermVisitor visitor)
    {
        visitor.visitBooleanTerm(this);
    }
}

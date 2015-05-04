package gov.va.med.srcalc.domain.model;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 * <p>A {@link ModelTerm} for a {@link BooleanVariable}.</p>
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

    public void setVariable(BooleanVariable variable)
    {
        fVariable = Objects.requireNonNull(variable);
    }
    
    @Override
    public double getSummand(final Value inputValue)
    {
        final boolean isTrue = Boolean.TRUE.equals(inputValue.getValue());
        
        return isTrue ? getCoefficient() : 0.0;
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
}

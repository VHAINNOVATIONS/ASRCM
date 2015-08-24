package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.NumericalValue;
import gov.va.med.srcalc.domain.calculation.Value;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 * <p>A {@link ModelTerm} for a {@link NumericalVariable}.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Embeddable
public final class NumericalTerm extends SingleVariableTerm
{
    private NumericalVariable fVariable;
    
    /**
     * For reflection-based construction only. Business code should use
     * {@link #NumericalTerm(NumericalVariable, float)}.
     */
    NumericalTerm()
    {
    }
    
    /**
     * Constructs an instance.
     * @param variable the variable for this term
     * @param coefficient the coefficient to be multiplied by this term's value
     */
    public NumericalTerm(
            final NumericalVariable variable, final float coefficient)
    {
        super(coefficient);
        fVariable = Objects.requireNonNull(variable);
    }

    @Override
    @ManyToOne(optional = false)
    public NumericalVariable getVariable()
    {
        return fVariable;
    }

    /**
     * For reflection-based construction only.
     * @deprecated because code should not explicitly call this method
     */
    @Deprecated
    void setVariable(final NumericalVariable variable)
    {
        fVariable = Objects.requireNonNull(variable);
    }
    
    @Override
    public float getSummand(final Value inputValue)
    {
        try
        {
            final NumericalValue value = (NumericalValue)inputValue;
            
            return value.getValue().floatValue() * getCoefficient();
        }
        catch (ClassCastException ex)
        {
            throw new IllegalArgumentException("Value was not a NumericalValue", ex);
        }
    }
    
    @Override
    public boolean equals(Object o)
    {
        // Performance optimization.
        if (o == this)
        {
            return true;
        }
        
        if (o instanceof NumericalTerm)
        {
            return baseEquals((NumericalTerm)o);
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
        return String.format("%s*%s", getVariable(), getCoefficient());
    }
    
    @Override
    public void accept(final ModelTermVisitor visitor)
    {
        visitor.visitNumericalTerm(this);
    }
}

package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.ProcedureValue;
import gov.va.med.srcalc.domain.calculation.Value;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 * <p>A {@link ModelTerm} for a {@link ProcedureVariable}.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Embeddable
public final class ProcedureTerm extends SingleVariableTerm
{
    private ProcedureVariable fVariable;
    
    /**
     * For reflection-based construction only. Business code should use
     * {@link #ProcedureTerm(ProcedureVariable, float)}.
     */
    ProcedureTerm()
    {
    }
    
    public ProcedureTerm(
            final ProcedureVariable variable, final float coefficient)
    {
        super(coefficient);
        fVariable = Objects.requireNonNull(variable);
    }

    @ManyToOne(optional = false)
    public ProcedureVariable getVariable()
    {
        return fVariable;
    }

    /**
     * For reflection-based construction only.
     * @deprecated because code should not explicitly call this method
     */
    @Deprecated
    void setVariable(final ProcedureVariable variable)
    {
        fVariable = Objects.requireNonNull(variable);
    }
    
    @Override
    public float getSummand(final Value inputValue)
    {
        try
        {
            final ProcedureValue value = (ProcedureValue)inputValue;
            return (value.getValue().getRvu() * getCoefficient());
        }
        catch (ClassCastException ex)
        {
            throw new IllegalArgumentException("Value was not a ProcedureValue", ex);
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
        
        if (o instanceof ProcedureTerm)
        {
            return baseEquals((ProcedureTerm)o);
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
        return String.format("%s.rvu*%s", getVariable(), getCoefficient());
    }
    
    @Override
    public void accept(final ModelTermVisitor visitor)
    {
        visitor.visitProcedureTerm(this);
    }
}

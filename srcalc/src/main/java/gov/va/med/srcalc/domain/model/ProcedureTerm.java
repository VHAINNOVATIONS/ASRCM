package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.ProcedureVariable;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

/**
 * <p>A {@link ModelTerm} for a {@link ProcedureVariable}.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
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

    public void setVariable(ProcedureVariable variable)
    {
        fVariable = Objects.requireNonNull(variable);
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
}

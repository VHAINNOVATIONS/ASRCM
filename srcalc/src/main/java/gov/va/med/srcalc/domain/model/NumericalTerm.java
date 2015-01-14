package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.NumericalVariable;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

/**
 * <p>A {@link ModelTerm} for a {@link NumericalVariable}.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
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
    
    public NumericalTerm(
            final NumericalVariable variable, final float coefficient)
    {
        super(coefficient);
        fVariable = Objects.requireNonNull(variable);
    }

    @ManyToOne(optional = false)
    public NumericalVariable getVariable()
    {
        return fVariable;
    }

    public void setVariable(NumericalVariable variable)
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
}

package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.BooleanVariable;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

/**
 * <p>A {@link ModelTerm} for a {@link BooleanVariable}.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
public final class BooleanTerm extends ModelTerm
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
    @Transient
    public Set<Variable> getRequiredVariables()
    {
        return CollectionUtils.<Variable>unmodifiableSet(fVariable);
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
            final BooleanTerm other = (BooleanTerm)o;
            return
                    baseEquals(other) &&
                    // Variable is non-null.
                    this.getVariable().equals(other.getVariable());
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

package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.Variable;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * <p>A constant term. Simply adds a constant to a risk model. (Each risk model
 * will have one, and only one, of these.)</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
public final class ConstantTerm extends ModelTerm
{
    /**
     * Mainly intended for reflection-based construction. Business code should
     * use {@link #ConstantTerm(float)}.
     */
    ConstantTerm()
    {
    }

    public ConstantTerm(final float coefficient)
    {
        super(coefficient);
    }

    @Override
    @Transient
    public Set<Variable> getRequiredVariables()
    {
        return Collections.emptySet();
    }
    
    @Override
    public boolean equals(Object o)
    {
        // Performance optimization.
        if (o == this)
        {
            return true;
        }
        
        if (o instanceof ConstantTerm)
        {
            final ConstantTerm other = (ConstantTerm)o;
            return baseEquals(other);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getCoefficient());
    }
}

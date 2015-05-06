package gov.va.med.srcalc.domain.model;

import java.util.*;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.google.common.collect.ImmutableSet;

/**
 * <p>A constant term. Simply adds a constant to a risk model. (Each risk model
 * will have one, and only one, of these.)</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Embeddable
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
    public ImmutableSet<Variable> getRequiredVariables()
    {
        return ImmutableSet.of();
    }
    
    @Override
    public double getSummand(final Map<Variable, Value> inputValues)
    {
        return getCoefficient();
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
    
    @Override
    public String toString()
    {
        return String.format("c=%s", getCoefficient());
    }
}

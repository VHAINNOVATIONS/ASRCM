package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.Value;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.util.NoNullSet;

import java.util.*;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

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
    public NoNullSet<Variable> getRequiredVariables()
    {
        // Two-line construction to get type arguments right.
        final Set<Variable> emptySet = Collections.emptySet();
        return NoNullSet.fromSet(emptySet);
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

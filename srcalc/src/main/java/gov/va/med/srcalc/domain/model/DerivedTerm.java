package gov.va.med.srcalc.domain.model;

import java.util.*;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.calculation.Value;
import gov.va.med.srcalc.util.MissingValuesException;

/**
 * <p>A {@link ModelTerm} derived via a {@link Rule}. A common use-case is
 * a summand added based on the values of two different variables.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Embeddable
public final class DerivedTerm extends ModelTerm
{
    private Rule fRule;
    
    /**
     * Mainly intended for reflection-based construction.
     */
    DerivedTerm()
    {
    }
    
    public DerivedTerm(final float coefficient, final Rule rule)
    {
        super(coefficient);
        setRule(rule);
    }

    /**
     * Returns the {@link Rule}.
     */
    @ManyToOne
    public Rule getRule()
    {
        return fRule;
    }
    
    /**
     * Set the current rule for this term.
     * @param rule A {@link Rule}.
     */
    void setRule(final Rule rule)
    {
        this.fRule = Objects.requireNonNull(rule);
    }

    @Transient
    @Override
    public ImmutableSet<Variable> getRequiredVariables()
    {
        return fRule.getRequiredVariables();
    }

    @Override
    public double getSummand(final Map<Variable, Value> inputValues) throws MissingValuesException
    {
        return fRule.apply(
                new Rule.EvaluationContext(getCoefficient(), inputValues));
    }
    
    @Override
    public boolean equals(Object o)
    {
        // Performance optimization.
        if (o == this)
        {
            return true;
        }
        
        if (o instanceof DerivedTerm)
        {
            final DerivedTerm other = (DerivedTerm)o;
            return
                    baseEquals(other) &&
                    this.getRule().equals(other.getRule());
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public String toString()
    {
        // Normally we put condition->coefficient, but here put coefficient->
        // rule because the coefficient is just an input to the rule.
        return String.format("%s->[%s]", getCoefficient(), fRule.getDisplayName());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getCoefficient(), getRule());
    }
    
    @Override
    public void accept(final ModelTermVisitor visitor)
    {
        visitor.visitDerivedTerm(this);
    }
}

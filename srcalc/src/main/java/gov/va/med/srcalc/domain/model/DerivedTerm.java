package gov.va.med.srcalc.domain.model;

import java.util.*;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.calculation.Value;
import gov.va.med.srcalc.util.MissingValuesException;

/**
 * <p>A {@link ModelTerm} derived via a {@link Rule}. A common use-case is a summand added
 * based on the values of two different variables. Presents an immutable public
 * interface.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Embeddable
public final class DerivedTerm extends ModelTerm
{
    private Rule fRule;
    
    /**
     * Intended only for reflection-based construction. Business code should call
     * {@link #DerivedTerm(float, Rule)}.
     */
    DerivedTerm()
    {
    }
    
    /**
     * Constructs an instance.
     * @see ModelTerm#ModelTerm(float)
     * @param coefficient
     * @param rule
     */
    public DerivedTerm(final float coefficient, final Rule rule)
    {
        super(coefficient);
        setRuleInternal(rule);
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
     * Sets the rule, ensuring non-null.
     */
    private void setRuleInternal(final Rule rule)
    {
        this.fRule = Objects.requireNonNull(rule);
    }
    
    /**
     * Set the rule for this term. For reflection-based construction only.
     * @param rule A {@link Rule}.
     * @deprecated because code should not explicitly call this method
     */
    @Deprecated
    void setRule(final Rule rule)
    {
        setRuleInternal(rule);
    }

    @Transient
    @Override
    public ImmutableSet<Variable> getRequiredVariables()
    {
        return fRule.getRequiredVariables();
    }

    @Override
    public float getSummand(final Map<Variable, Value> inputValues) throws MissingValuesException
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

package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.Value;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * An object which evaluates a {@link Value} to true or false using a Spring
 * Expression Language (SPeL) expression.
 */
@Embeddable
public final class ValueMatcher
{
    private Variable fVariable;
    private Expression fBooleanExpression;
    private boolean fEnabled;
    
    /**
	 * Mainly intended for reflection-based construction.
	 */
    ValueMatcher()
    {
        // Set the summand expression to avoid a NullPointerException from Hibernate.
        fBooleanExpression = new SpelExpressionParser().parseExpression("unset");
    }
    
    /**
     * Constructs an instance.
     * @param variable
     * @param booleanExpression
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if the given expression is not parsable
     */
    public ValueMatcher(final Variable variable, final String booleanExpression, final boolean applyMatcher)
    {
        fVariable = Objects.requireNonNull(variable);
        fBooleanExpression = parseBooleanExpression(booleanExpression);
        fEnabled = applyMatcher;
    }

    /**
     * Parse the designated expression into a SPEL Expression.
     * 
     * @param summandExpression
     *            The expression to be parsed into a boolean expression.
     * @return A valid, parsed SPEL Expression
     */
    private Expression parseBooleanExpression(final String booleanExpression)
    {
        final SpelExpressionParser parser = new SpelExpressionParser();
        try
        {
            return parser.parseExpression(Objects.requireNonNull(booleanExpression));
        }
        catch (final ParseException ex)
        {
            throw new IllegalArgumentException("Could not parse given expression.", ex);
        }
    }

    /**
     * Returns the {@link Variable} whose {@link Value} this object matches.
     * @return never null
     */
    @ManyToOne(targetEntity = AbstractVariable.class)
    public Variable getVariable()
    {
        return fVariable;
    }

    void setVariable(final Variable variable)
    {
    	this.fVariable = variable;
    }
    
    /**
     * Returns the configured boolean expression as a String.
     */
    @Basic
    public String getBooleanExpression()
    {
        return fBooleanExpression.getExpressionString();
    }
    
    void setBooleanExpression(final String booleanExpression)
    {
    	fBooleanExpression = parseBooleanExpression(booleanExpression);
    }
    
    @Transient
    public Expression getParsedExpression()
    {
        return fBooleanExpression;
    }
    
    /**
     * Returns true if this matcher should be evaluated and false if it should not
     * be evaluated.
     */
    @Basic
    public boolean isEnabled()
    {
        return fEnabled;
    }
    
    void setEnabled(final boolean enabled)
    {
        fEnabled = enabled;
    }
    
    /**
     * Evaluate this ValueMatcher's boolean expression based on the given context and value.
     * @param context the EvaluationContext used to evaluate this matcher against
     * @param value the value for this matcher's variable
     * @return the boolean value to which the expression evaluates
     */
    public boolean evaluate(final EvaluationContext context, final Value value)
    {
        if(fEnabled && !fBooleanExpression.getExpressionString().isEmpty())
        {
            return fBooleanExpression.getValue(context, value, Boolean.class);
        }
        else
        {
            return true;
        }
    }
    
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof ValueMatcher)
        {
            final ValueMatcher other = (ValueMatcher)obj;
            return
                    // Note that getBooleanExpression() returns the String, not
                    // the Expression object itself.
                    Objects.equals(this.getVariable(), other.getVariable()) &&
                    Objects.equals(this.getBooleanExpression(), other.getBooleanExpression()) &&
                    this.isEnabled() == other.isEnabled();
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getVariable(), getBooleanExpression(), isEnabled());
    }
    
    @Override
    public String toString()
    {
        return String.format(
                "ValueMatcher on %s: \"%s\"", getVariable(), getBooleanExpression());
    }
}

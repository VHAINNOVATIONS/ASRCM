package gov.va.med.srcalc.domain.model;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import gov.va.med.srcalc.domain.variable.AbstractVariable;
import gov.va.med.srcalc.domain.variable.Value;
import gov.va.med.srcalc.domain.variable.Variable;

/**
 * An object which evaluates a {@link Value} to true or false using a Spring
 * Expression Language (SPeL) expression.
 */
@Embeddable
public class ValueMatcher
{
    private Variable fVariable;
    private Expression fBooleanExpression;
    
    /**
	 * Mainly intended for reflection-based construction.
	 */
    ValueMatcher()
    {    	
    }
    
    /**
     * Constructs an instance.
     * @param variable
     * @param booleanExpression
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if the given expression is not parsable
     */
    public ValueMatcher(final Variable variable, final String booleanExpression)
    {
        fVariable = Objects.requireNonNull(variable);
        fBooleanExpression = parseBooleanExpression(booleanExpression);
    }

    /**
     * Parse the designated expression into a SPEL Expression.
     * @param summandExpression The expression to be parsed into a boolean expression.
     * @return A valid, parsed SPEL Expression
     */
	private Expression parseBooleanExpression(final String booleanExpression)
	{
		final SpelExpressionParser parser = new SpelExpressionParser();
        try
        {
            return parser.parseExpression(
                    Objects.requireNonNull(booleanExpression));
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
    
    public boolean evaluate(final EvaluationContext context, final Value value)
    {
        return fBooleanExpression.getValue(context, value, Boolean.class);
    }
    
    @Override
    public String toString()
    {
        return String.format(
                "ValueMatcher on %s: \"%s\"", getVariable(), getBooleanExpression());
    }
}

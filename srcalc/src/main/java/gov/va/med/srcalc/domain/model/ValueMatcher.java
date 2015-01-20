package gov.va.med.srcalc.domain.model;

import java.util.Objects;

import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import gov.va.med.srcalc.domain.variable.Value;
import gov.va.med.srcalc.domain.variable.Variable;

/**
 * An object which evaluates a {@link Value} to true or false using a Spring
 * Expression Language (SPeL) expression.
 */
public class ValueMatcher
{
    private final Variable fVariable;
    private final Expression fBooleanExpression;
    
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
        final SpelExpressionParser parser = new SpelExpressionParser();
        try
        {
            fBooleanExpression = parser.parseExpression(
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
    public Variable getVariable()
    {
        return fVariable;
    }

    /**
     * Returns the configured boolean expression as a String.
     */
    public String getBooleanExpression()
    {
        return fBooleanExpression.getExpressionString();
    }
    
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

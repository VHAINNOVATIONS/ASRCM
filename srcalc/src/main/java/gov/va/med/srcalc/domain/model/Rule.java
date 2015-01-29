package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.Value;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.util.NoNullSet;

import java.util.*;

import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * <p>Encapsulates a configured rule to use in a model calculation, such as
 * "multiply the age by a coefficient if the patient is totally dependent" or
 * "add a constant to the sum if the patient's current weight is less than 90%
 * of her weight 6 months ago."</p>
 * 
 * <p>Note: This is a separate class from {@link DerivedTerm} to allow the same
 * Rule to be shared by multiple instances.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class Rule
{
    private final List<ValueMatcher> fMatchers;
    private final Expression fSummandExpression;
    
    /**
     * Constructs an instance.
     * @param matchers
     * @param summandExpression
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if the given expression is not parsable
     */
    public Rule(
            final List<ValueMatcher> matchers, final String summandExpression)
    {
        fMatchers = Objects.requireNonNull(matchers);
        final SpelExpressionParser parser = new SpelExpressionParser();
        try
        {
            fSummandExpression = parser.parseExpression(
                    Objects.requireNonNull(summandExpression));
        }
        catch (final ParseException ex)
        {
            throw new IllegalArgumentException("Could not parse given expression.", ex);
        }
    }
    
    /**
     * The {@link ValueMatcher}s that form the conditional part of the rule.
     * Order is important: each matcher may use SpEL variable references to
     * previously-matched values.
     */
    public List<ValueMatcher> getMatchers()
    {
        return fMatchers;
    }

    /**
     * The SpEL expression that calculates the summand. May use variable
     * references to the values matched in ValueMatchers.
     */
    public String getSummandExpression()
    {
        return fSummandExpression.getExpressionString();
    }

    /**
     * Returns all {@link Variable}s required for evaluating the Rule.
     * @return an unmodifiable set
     */
    public NoNullSet<Variable> getRequiredVariables()
    {
        final HashSet<Variable> variables = new HashSet<>();
        for (final ValueMatcher vm : fMatchers)
        {
            variables.add(vm.getVariable());
        }
        return NoNullSet.fromSet(Collections.unmodifiableSet(variables));
    }

    /**
     * Applies the Rule to the given context.
     * @param context determines the context in which to evaluate the rule,
     * including {@link Value}s and the coefficient
     * @return the summand
     */
    public double apply(final EvaluationContext context)
    {
        /* Match all the values */
        final HashMap<String, Object> matchedValues = new HashMap<>();
        final StandardEvaluationContext ec = new StandardEvaluationContext();
        for (final ValueMatcher condition : fMatchers)
        {
            // TODO: replace each expression's "root object" with the actual
            // value instead of the Value wrapper object.
            
            // Will return null if there is no value for the given variable.
            // TODO: properly handle missing Values
            final Value matchedValue = context.getValues().get(condition.getVariable());

            if (matchedValue != null && condition.evaluate(ec, matchedValue))
            {
                matchedValues.put(matchedValue.getVariable().getKey(), matchedValue);
            }
            else
            {
                // No match: return 0
                return 0.0;
            }
            
            // Update the SpEL evaluation context with the matched values so far.
            ec.setVariables(matchedValues);
        }
        
        /* We matched them all: now just calculate the summand. */
        ec.setVariable("coefficient", context.getCoefficient());
        return fSummandExpression.getValue(ec, Double.class);
    }
    
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof Rule)
        {
            final Rule other = (Rule)obj;
            return
                    // Note that getSummandExpression() returns the String, not
                    // the Expression object itself.
                    this.getMatchers().equals(other.getMatchers()) &&
                    this.getSummandExpression().equals(other.getSummandExpression());
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getMatchers(), getSummandExpression());
    }
    
    /**
     * The context in which to evaluate a {@link Rule}. See {@link
     * Rule#apply(EvaluationContext)}.
     */
    public static final class EvaluationContext
    {
        final float fCoefficient;
        final Map<Variable, Value> fValues;
        
        public EvaluationContext(
                final float coefficient, final Map<Variable, Value> values)
        {
            fCoefficient = coefficient;
            fValues = values;
        }

        public float getCoefficient()
        {
            return fCoefficient;
        }

        public Map<Variable, Value> getValues()
        {
            return fValues;
        }
    }
}

package gov.va.med.srcalc.domain.model;

import java.util.*;

import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import gov.va.med.srcalc.domain.variable.Value;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.util.NoNullSet;

/**
 * <p>A {@link ModelTerm} derived from one or more variables. A common use-case is
 * a summand added based on the values of two different variables.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class DerivedTerm extends ModelTerm
{
    private Collection<ValueMatcher> fMatchers;
    private String fSummandExpression;
    private Expression fParsedExpression;
    
    public DerivedTerm(
            final float coefficient,
            final Collection<ValueMatcher> matchers,
            final String summandExpression)
    {
        super(coefficient);
        fMatchers = Objects.requireNonNull(matchers);
        fSummandExpression = Objects.requireNonNull(summandExpression);
        final ExpressionParser parser = new SpelExpressionParser();
        fParsedExpression = parser.parseExpression(summandExpression);
    }
    
    public Collection<ValueMatcher> getMatchers()
    {
        return fMatchers;
    }

    public String getSummandExpression()
    {
        return fSummandExpression;
    }

    @Override
    public NoNullSet<Variable> getRequiredVariables()
    {
        final HashSet<Variable> variables = new HashSet<>();
        for (final ValueMatcher vm : fMatchers)
        {
            variables.add(vm.getVariable());
        }
        return NoNullSet.fromSet(Collections.unmodifiableSet(variables));
    }

    @Override
    public double getSummand(final Map<Variable, Value> inputValues)
    {
        final ExpressionParser parser = new SpelExpressionParser();
        
        // Match all the values
        final HashMap<String, Object> matchedValues = new HashMap<>();
        for (final ValueMatcher condition : fMatchers)
        {
            final Value matchedValue = inputValues.get(condition.getVariable());
            if (matchedValue == null)
            {
                // No match: return 0
                return 0.0;
            }

            final Expression conditionExpression =
                    parser.parseExpression(condition.getBooleanExpression());
            if (conditionExpression.getValue(matchedValue, Boolean.class))
            {
                matchedValues.put(matchedValue.getVariable().getKey(), matchedValue);
            }
            else
            {
                // No match: return 0
                return 0.0;
            }
        }
        
        // We matched them all: now just calculate the summand.
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(matchedValues);
        context.setVariable("coefficient", getCoefficient());
        
        return fParsedExpression.getValue(context, Double.class);
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
        return Objects.hash(getCoefficient(), getMatchers(), getSummandExpression());
    }
    
}

package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.util.MissingValuesException;
import gov.va.med.srcalc.util.NoNullSet;

import java.util.*;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Entity
@Table(name = "rule")
public final class Rule
{
	private int fId;
    private List<ValueMatcher> fMatchers;
    private Expression fSummandExpression;
    private boolean fRequired;
    
	/**
	 * Mainly intended for reflection-based construction.
	 */
    Rule()
    {
    }
    
    /**
     * Constructs an instance.
     * @param matchers
     * @param summandExpression
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if the given expression is not parsable
     */
    public Rule(
            final List<ValueMatcher> matchers, final String summandExpression, final boolean required)
    {
        fMatchers = Objects.requireNonNull(matchers);
        fSummandExpression = parseSummandExpression(summandExpression);
        fRequired = required;
    }
    
    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id
    public int getId()
    {
        return fId;
    }

    /**
     * For reflection-based construction only. Business code should never modify
     * the surrogate key as it is generated from the database.
     */
    void setId(final int id)
    {
        this.fId = id;
    }
    
    @ElementCollection(fetch = FetchType.EAGER) // eager-load due to close association
    // Override strange defaults.
    @CollectionTable(
            name = "rule_value_matcher",
            joinColumns = @JoinColumn(name = "rule_id"))
    /**
     * The {@link ValueMatcher}s that form the conditional part of the rule.
     * Order is important: each matcher may use SpEL variable references to
     * previously-matched values.
     */
    public List<ValueMatcher> getMatchers()
    {
        return fMatchers;
    }
    
    void setMatchers(final List<ValueMatcher> matchers)
    {
    	this.fMatchers = matchers;
    }

    /**
     * The SpEL expression that calculates the summand. May use variable
     * references to the values matched in ValueMatchers.
     */
    @Basic
    public String getSummandExpression()
    {
        return fSummandExpression.getExpressionString();
    }
    
    void setSummandExpression(final String summandExpression)
    {
    	this.fSummandExpression = parseSummandExpression(summandExpression);
    }

    /**
     * Should we bypass this rule if values are missing.
     */
    @Basic
    private boolean isRequired()
	{
		return fRequired;
	}
	
	void setRequired(final boolean required)
	{
		fRequired = required;
	}

    
    /**
     * Parse the designated expression into a SPEL Expression.
     * @param summandExpression The expression to be parsed into a summand expression.
     * @return A valid, parsed SPEL Expression
     */
	private Expression parseSummandExpression(final String summandExpression)
	{
		final SpelExpressionParser parser = new SpelExpressionParser();
        try
        {
            return parser.parseExpression(
                    Objects.requireNonNull(summandExpression));
        }
        catch (final ParseException ex)
        {
            throw new IllegalArgumentException("Could not parse given expression.", ex);
        }
	}
	
    /**
     * Returns all {@link Variable}s required for evaluating the Rule.
     * @return an unmodifiable set
     */
    @Transient
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
    public double apply(final EvaluationContext context) throws MissingValuesException
    {
        /* Match all the values */
        final StandardEvaluationContext ec = new StandardEvaluationContext();
        final MissingValuesException missingValues = new MissingValuesException(
        		"The calculation is missing values.", new ArrayList<MissingValueException>());
        // Pass over the matcher list twice. Once to ensure all values are present.
        // Twice to actually evaluate the value matchers.
        for (final ValueMatcher condition : fMatchers)
        {
            // TODO: replace each expression's "root object" with the actual
            // value instead of the Value wrapper object.
            
            // Will return null if there is no value for the given variable.
            final Value matchedValue = context.getValues().get(condition.getVariable());
            if(matchedValue == null)
            {
            	if(isRequired())
            	{
            		missingValues.getMissingValues().add(new MissingValueException(
            				"Missing value for " + condition.getVariable().getKey(),
                    		condition.getVariable()));
            		continue;
            	}
            	// If the variable is not required, there is no coefficient added to the calculation.
            	// This essentially makes the rule evaluate to false;
            	return 0.0;
            }
        	
        }
        if(missingValues.getMissingValues().size() > 0)
        {
        	throw missingValues;
        }
        final HashMap<String, Object> matchedValues = new HashMap<>();
        for (final ValueMatcher condition : fMatchers)
        {
        	final Value matchedValue = context.getValues().get(condition.getVariable());
        	if (condition.evaluate(ec, matchedValue))
            {
                matchedValues.put(matchedValue.getVariable().getKey(), matchedValue);
            }
            else
            {
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

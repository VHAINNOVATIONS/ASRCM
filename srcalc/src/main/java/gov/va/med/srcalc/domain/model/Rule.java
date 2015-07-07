package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.Value;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.MissingValuesException;
import gov.va.med.srcalc.util.Preconditions;

import java.util.*;

import javax.persistence.*;

import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

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
    public static final int MAX_MATCHERS = 10;
    
    private int fId;
    private List<ValueMatcher> fMatchers;
    private Expression fSummandExpression;
    private boolean fRequired;
    private String fDisplayName;
    
	/**
	 * Mainly intended for reflection-based construction.
	 */
    Rule()
    {
        // Set the summand expression to avoid a NullPointerException from Hibernate.
        fSummandExpression = parseSummandExpression("unset");
        fDisplayName = "unset";
    }
    
    /**
     * Constructs an instance.
     * @param matchers
     * @param summandExpression
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if the given expression is not parsable
     */
    public Rule(
            final List<ValueMatcher> matchers, final String summandExpression,
            final boolean required, final String displayName)
    {
        fMatchers = Objects.requireNonNull(matchers);
        fSummandExpression = parseSummandExpression(summandExpression);
        fRequired = required;
        fDisplayName = displayName;
    }
    
    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id
    @GeneratedValue
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
    
    public void setMatchers(final List<ValueMatcher> matchers)
    {
    	this.fMatchers = matchers;
    }

    /**
     * The SpEL expression that calculates the summand. May use variable
     * references to the values matched in ValueMatchers.
     */
    @Basic
    @Column(nullable = false)
    public String getSummandExpression()
    {
        return fSummandExpression.getExpressionString();
    }
    
    public void setSummandExpression(final String summandExpression)
    {
    	this.fSummandExpression = parseSummandExpression(summandExpression);
    }

    /**
     * Should we bypass this rule if values are missing.
     */
    @Basic
    public boolean isRequired()
    {
        return fRequired;
    }
    
    public void setRequired(final boolean required)
    {
        fRequired = required;
    }
    
    /**
     * The rule's name to display to the user
     * @return
     */
    @Basic
    @Column(
            length = DisplayNameConditions.DISPLAY_NAME_MAX,
            nullable = false)
    public String getDisplayName()
    {
        return fDisplayName;
    }
    
    /**
     * Sets the name of the rule for display to the user.
     * @throws IllegalArgumentException if the given name is empty, over
     * {@link DisplayNameConditions#DISPLAY_NAME_MAX} characters, or does not match
     * {@link DisplayNameConditions#VALID_DISPLAY_NAME_REGEX}
     */
    public void setDisplayName(final String displayName)
    {
        Preconditions.requireWithin(displayName, 1, DisplayNameConditions.DISPLAY_NAME_MAX);
        Preconditions.requireMatches(displayName, "displayName", DisplayNameConditions.VALID_DISPLAY_NAME_PATTERN);
        fDisplayName = displayName;
    }
    
    /**
     * Parse the designated expression into a SPEL Expression.
     * 
     * @param summandExpression
     *            The expression to be parsed into a summand expression.
     * @return A valid, parsed SPEL Expression
     */
    private Expression parseSummandExpression(final String summandExpression)
    {
        final SpelExpressionParser parser = new SpelExpressionParser();
        try
        {
            return parser.parseExpression(Objects.requireNonNull(summandExpression));
        }
        catch (final ParseException ex)
        {
            throw new IllegalArgumentException("Could not parse given expression.", ex);
        }
    }
	
    /**
     * Returns all {@link Variable}s required for evaluating the Rule.
     * @return an ImmutableSet
     */
    @Transient
    public ImmutableSet<Variable> getRequiredVariables()
    {
        final HashSet<Variable> variables = new HashSet<>();
        for (final ValueMatcher vm : fMatchers)
        {
            variables.add(vm.getVariable());
        }
        return ImmutableSet.copyOf(variables);
    }

    /**
     * Returns the sorted keys for all required variables from {@link #getRequiredVariables()}.
     * @return an ImmutableSet
     */
    @Transient
    public ImmutableSet<String> getRequiredVariableKeys()
    {
        final ImmutableSet<Variable> variables = getRequiredVariables();
        final SortedSet<String> sortedKeys = new TreeSet<String>();
        for(final Variable var: variables)
        {
            sortedKeys.add(var.getKey());
        }
        return ImmutableSet.copyOf(sortedKeys);
    }
    
    /**
     * Returns a String in the form of "#a, #b, #c" that will 
     */
    @Transient
    public String getExpressionVariablesString()
    {
        return String.format("#%s", Joiner.on(", #"));
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
            matchedValues.put(matchedValue.getVariable().getKey(), matchedValue.getValue());
            // Update the Spel evaluation context with the previous and current values
            ec.setVariables(matchedValues);
            if (!condition.evaluate(ec, matchedValue))
            {
                return 0.0;
            }
        }
        
        /* We matched them all: now just calculate the summand. */
        ec.setVariable("coefficient", context.getCoefficient());
        return fSummandExpression.getValue(ec, Double.class);
    }
    
    /**
     * Returns a String representation of this rule. The format is unspecified,
     * but it will contain the summand expression and the matchers.
     */
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", fId)
                // fSummandExpression has a bad toString(), use getSummandExpression()
                .add("summandExpression", getSummandExpression())
                .add("required", fRequired)
                .add("matchers", fMatchers)
                .toString();
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
                    this.getDisplayName().equals(other.getDisplayName()) &&
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
        return Objects.hash(getDisplayName(), getMatchers(), getSummandExpression());
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

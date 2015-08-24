package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.ValueMatcher;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.ModelInspectionService;

/**
 * <p>Builds a {@link ValueMatcher} from incrementally-
 * specified component parts.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class ValueMatcherBuilder
{
    private String fVariableKey;
    private String fBooleanExpression;
    private boolean fEnabled;
    
    /**
     * Constructs an instance with empty strings for the variable key and
     * the boolean expression.
     */
    public ValueMatcherBuilder()
    {
        fVariableKey = "";
        fBooleanExpression = "";
    }
    
    /**
     * Constructs an instance using the specified variable key.
     * @param variableKey the variable key that this ValueMatcherBuilder is tied to.
     */
    public ValueMatcherBuilder(final String variableKey)
    {
        fVariableKey = variableKey;
        fBooleanExpression = "";
    }
    
    /**
     * Factory method to create an instance based on a prototype Category.
     * @return a new builder that will build categories equivalent to the given
     * prototype
     */
    public static ValueMatcherBuilder fromPrototype(final ValueMatcher prototype)
    {
        return new ValueMatcherBuilder(prototype.getVariable().getKey())
            .setBooleanExpression(prototype.getBooleanExpression())
            .setEnabled(prototype.isExpressionEnabled());
    }

    /**
     * Returns the variable key that this ValueMatcherBuilder references.
     */
    public String getVariableKey()
    {
        return fVariableKey;
    }

    /**
     * Sets the variable key that this ValueMatcherBuilder references.
     * @param variableKey
     */
    public ValueMatcherBuilder setVariableKey(final String variableKey)
    {
        fVariableKey = variableKey;
        return this;
    }

    /**
     * Returns the boolean expression to use for constructed {@link ValueMatcher}.
     */
    public String getBooleanExpression()
    {
        return fBooleanExpression;
    }

    /**
     * Sets the boolean expression to use for constructed {@link ValueMatcher}.
     * @param booleanExpression
     */
    public ValueMatcherBuilder setBooleanExpression(final String booleanExpression)
    {
        fBooleanExpression = booleanExpression;
        return this;
    }
    
    /**
     * Returns whether or not the constructed {@link ValueMatcher} should be evaluated.
     */
    public boolean isEnabled()
    {
        return fEnabled;
    }
    
    /**
     * Sets whether or not the constructed {@link ValueMatcher} should be evaluated.
     * @param enabled
     */
    public ValueMatcherBuilder setEnabled(final boolean enabled)
    {
        fEnabled = enabled;
        return this;
    }
    
    /**
     * Returns a {@link ValueMatcher} that is built from the calling object.
     * @param modelService the admin service used to retrieve the necessary variable
     * @throws InvalidIdentifierException if no variable exists with the specified key
     */
    public ValueMatcher buildNew(final ModelInspectionService modelService) throws InvalidIdentifierException
    {
        return new ValueMatcher(
                modelService.getVariable(fVariableKey),
                fBooleanExpression,
                fEnabled);
    }
}

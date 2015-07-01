package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.ValueMatcher;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;

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
     * Returns variable key that this ValueMatcherBuilder references
     */
    public String getVariableKey()
    {
        return fVariableKey;
    }

    public ValueMatcherBuilder setVariableKey(final String variableKey)
    {
        fVariableKey = variableKey;
        return this;
    }

    /**
     * Returns the expression used to evaluate the constructed {@link ValueMatcher}.
     */
    public String getBooleanExpression()
    {
        return fBooleanExpression;
    }

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
    
    public ValueMatcherBuilder setEnabled(final boolean enabled)
    {
        fEnabled = enabled;
        return this;
    }
    
    @Override
    public boolean equals(final Object other)
    {
        if(other instanceof ValueMatcherBuilder)
        {
            final ValueMatcherBuilder otherBuilder = (ValueMatcherBuilder) other;
            return this.fVariableKey.equals(otherBuilder.fVariableKey);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Returns a {@link ValueMatcher} that is built from the calling object.
     * @param adminService the admin service used to retrieve the necessary variable
     * @throws InvalidIdentifierException
     */
    public ValueMatcher buildNew(final AdminService adminService) throws InvalidIdentifierException
    {
        return new ValueMatcher(
                adminService.getVariable(fVariableKey),
                fBooleanExpression,
                fEnabled);
    }
}

package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.ValueMatcher;

public class ValueMatcherBuilder
{
    private String fVariableKey;
    private String fBooleanExpression;
    private boolean fEnabled;
    
    public ValueMatcherBuilder()
    {
        fVariableKey = "";
        fBooleanExpression = "";
    }
    
    /**
     * Factory method to create an instance based on a prototype Category.
     * @return a new builder that will build categories equivalent to the given
     * prototype
     */
    public static ValueMatcherBuilder fromPrototype(final ValueMatcher prototype)
    {
        return new ValueMatcherBuilder()
            .setVariableKey(prototype.getVariable().getKey())
            .setBooleanExpression(prototype.getBooleanExpression())
            .setEnabled(prototype.isEnabled());
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
}

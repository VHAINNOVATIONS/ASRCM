package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.Variable;

public class ValueMatcher
{
    private final Variable fVariable;
    private final String fBooleanExpression;
    
    public ValueMatcher(final Variable variable, final String booleanExpression)
    {
        fVariable = variable;
        fBooleanExpression = booleanExpression;
    }

    public Variable getVariable()
    {
        return fVariable;
    }

    public String getBooleanExpression()
    {
        return fBooleanExpression;
    }
}

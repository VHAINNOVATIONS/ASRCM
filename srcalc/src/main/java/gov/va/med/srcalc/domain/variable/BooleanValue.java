package gov.va.med.srcalc.domain.variable;

public class BooleanValue implements Value
{
    private final BooleanVariable fVariable;
    private final boolean fValue;

    public BooleanValue(final BooleanVariable variable, final boolean value)
    {
        fVariable = variable;
        fValue = value;
    }
    
    @Override
    public BooleanVariable getVariable()
    {
        return fVariable;
    }
    
    @Override
    public Boolean getValue()
    {
        return fValue;
    }
    
    @Override
    public String getDisplayString()
    {
        return fValue ? "Yes" : "No";
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %s", getVariable(), getValue());
    }
    
}
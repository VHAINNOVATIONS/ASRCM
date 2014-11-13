package gov.va.med.srcalc.domain.variable;

public class NumericalValue implements Value
{
    private final NumericalVariable fVariable;
    private final int fValue;
    
    public NumericalValue(final NumericalVariable variable, final int value)
    {
        fVariable = variable;
        fValue = value;
    }
    
    @Override
    public NumericalVariable getVariable()
    {
        return fVariable;
    }
    
    @Override
    public Integer getValue()
    {
        return fValue;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %d", getVariable(), getValue());
    }
}

package gov.va.med.srcalc.domain.variable;

public class MultiSelectValue implements Value
{
    private final MultiSelectVariable fVariable;
    private final MultiSelectOption fSelectedOption;
    
    public MultiSelectValue(final MultiSelectVariable variable, final MultiSelectOption selectedOption)
    {
        fVariable = variable;
        fSelectedOption = selectedOption;
    }
    
    @Override
    public MultiSelectVariable getVariable()
    {
        return fVariable;
    }
    
    public MultiSelectOption getSelectedOption()
    {
        return fSelectedOption;
    }
    
    @Override
    public MultiSelectOption getValue()
    {
        return getSelectedOption();
    }
    
    @Override
    public String getDisplayString()
    {
        return getSelectedOption().getValue();
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %s", getVariable(), getValue());
    }
}

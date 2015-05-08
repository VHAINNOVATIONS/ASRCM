package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.model.*;


public class MultiSelectValue implements DiscreteValue
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
    
    /**
     * Since a {@link MultiSelectOption} simply wraps a String, return just the
     * String for convenience.
     */
    @Override
    public String getValue()
    {
        return getSelectedOption().getValue();
    }
    
    @Override
    public String getDisplayString()
    {
        return getSelectedOption().getValue();
    }
    
    @Override
    public void accept(final ValueVisitor visitor)
    {
    	visitor.visitMultiSelect(this);
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %s", getVariable(), getSelectedOption());
    }
}

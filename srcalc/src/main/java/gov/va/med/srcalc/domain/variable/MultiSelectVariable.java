package gov.va.med.srcalc.domain.variable;

import java.util.List;

public class MultiSelectVariable extends Variable
{
    public enum DisplayType
    {
        Radio,
        Dropdown
    }
    
    private DisplayType fDisplayType;
    
    private List<MultiSelectOption> fOptions;
    
    public MultiSelectVariable()
    {
    }
    
    public MultiSelectVariable(
            String displayName, DisplayType displayType, List<MultiSelectOption> options)
    {
        super(displayName);
        fDisplayType = displayType;
        fOptions = options;
    }
    
    public DisplayType getDisplayType()
    {
        return fDisplayType;
    }
    
    public void setDisplayType(DisplayType displayType)
    {
        fDisplayType = displayType;
    }

    public List<MultiSelectOption> getOptions()
    {
        return fOptions;
    }

    public void setOptions(List<MultiSelectOption> options)
    {
        fOptions = options;
    }
    
    @Override
    public void accept(VariableVisitor visitor) throws Exception
    {
        visitor.visitMultiSelect(this);
    }
}

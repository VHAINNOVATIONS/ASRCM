package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.VariableGroup;

public abstract class DisplayItem
{
    private final String fDisplayName;
    private final VariableGroup fDisplayGroup;
    
    protected DisplayItem()
    {
        fDisplayName = "";
        fDisplayGroup = new VariableGroup("unset group", 0);
    }
    
    protected DisplayItem(final String displayName, final VariableGroup displayGroup)
    {
        fDisplayName = displayName;
        fDisplayGroup = displayGroup;
    }
    
    public String getDisplayName()
    {
        return fDisplayName;
    }
    
    public VariableGroup getDisplayGroup()
    {
        return fDisplayGroup;
    }
}

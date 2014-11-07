package gov.va.med.srcalc.domain.variable;

/**
 * Default implementation of a MultiSelectOption storing a simple display name.
 */
public class MultiSelectOption
{
    private String fDisplayName;
    
    public MultiSelectOption()
    {
    }
    
    public MultiSelectOption(String displayName)
    {
        fDisplayName = displayName;
    }
    
    public String getDisplayName()
    {
        return fDisplayName;
    }
    
    public void setDisplayName(String displayName)
    {
        fDisplayName = displayName;
    }
}

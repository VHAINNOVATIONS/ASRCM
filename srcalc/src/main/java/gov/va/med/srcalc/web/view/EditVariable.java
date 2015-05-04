package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.AbstractVariable;

public final class EditVariable
{
    private final String fKey;

    private String fDisplayName;
    
    private String fHelpText;
    
    private EditVariable(final String key)
    {
        fKey = key;
    }
    
    public static EditVariable fromVariable(final AbstractVariable variable)
    {
        final EditVariable ev = new EditVariable(variable.getKey());
        ev.setDisplayName(variable.getDisplayName());
        ev.setHelpText(variable.getHelpText());
        return ev;
    }

    /**
     * The key that uniquely identifies the variable, suitable for displaying
     * to an administrative user.
     */
    public String getKey()
    {
        return fKey;
    }

    public String getDisplayName()
    {
        return fDisplayName;
    }

    public void setDisplayName(String displayName)
    {
        fDisplayName = displayName;
    }
    
    public String getHelpText()
    {
        return fHelpText;
    }

    public void setHelpText(String helpText)
    {
        fHelpText = helpText;
    }

    /**
     * Sets all stored properties on the given Variable.
     * @param variable the variable to modify
     * @return the same Variable reference for convenience
     */
    public AbstractVariable setOntoVariable(final AbstractVariable variable)
    {
        variable.setDisplayName(fDisplayName);
        variable.setHelpText(fHelpText);
        return variable;
    }
}

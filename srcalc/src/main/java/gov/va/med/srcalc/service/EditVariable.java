package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.AbstractVariable;

import java.util.Arrays;
import java.util.List;

/**
 * A command object that updates a {@link AbstractVariable}'s properties. See
 * {@link AdminService#updateVariable(EditVariable)} for a primary usage.
 */
public final class EditVariable
{
    /**
     * Temporarily hardcode the list of variables we /plan/ to integrate. Once
     * we actually implement automatic retrieval from VistA, this hardcoded list
     * will go away.
     */
    private static final List<String> INTEGRATED_VARIABLE_NAMES = Arrays.asList(
            "Age", "BMI", "DNR", "Gender");
    
    private String fKey;

    private String fDisplayName;
    
    private EditVariable(final String key)
    {
        fKey = key;
    }
    
    public static EditVariable fromVariable(final AbstractVariable variable)
    {
        final EditVariable ev = new EditVariable(variable.getKey());
        ev.setDisplayName(variable.getDisplayName());
        return ev;
    }
    
    /**
     * Returns true if the variable is VistA-integrated. Updating the display
     * name for such variables will break VistA integration.
     */
    public boolean isIntegratedVariable()
    {
        return INTEGRATED_VARIABLE_NAMES.contains(fDisplayName);
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
    
    /**
     * Sets all stored properties on the given Variable.
     * @param variable the variable to modify
     * @return the same Variable reference for convenience
     */
    public AbstractVariable setOntoVariable(final AbstractVariable variable)
    {
        variable.setDisplayName(fDisplayName);
        return variable;
    }
}

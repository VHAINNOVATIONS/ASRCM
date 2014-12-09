package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.variable.Variable;

/**
 * A command object that updates a {@link Variable}'s properties. See
 * {@link AdminService#updateVariable(Variable)} for a primary usage.
 */
public class EditVariable
{
    private String fDisplayName;
    
    public static EditVariable fromVariable(final Variable variable)
    {
        final EditVariable ev = new EditVariable();
        ev.setDisplayName(variable.getDisplayName());
        return ev;
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
    public Variable setOntoVariable(final Variable variable)
    {
        variable.setDisplayName(fDisplayName);
        return variable;
    }
}

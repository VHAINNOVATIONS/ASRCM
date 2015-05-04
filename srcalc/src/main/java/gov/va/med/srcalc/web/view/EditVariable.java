package gov.va.med.srcalc.web.view;

import java.util.Collection;

import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.domain.model.VariableGroup;

/**
 * <p>Encapsulates the operation to update an existing variable.</p>
 * 
 * <p>Workflow:</p>
 * 
 * <ol>
 * <li>Construct an instance based on an existing variable.</li>
 * <li>Present the user with the initial values and the "reference data" such
 * as {@link #getAllGroups()}.</li>
 * <li>Have the user update the properties as desired.</li>
 * <li>Use {@link EditVariableValidator} to validate the user's edits.</li>
 * <li>Call {@link #applyToVariable(AbstractVariable)} to update an existing
 * variable with the new properties.</li>
 * </ol>
 */
public final class EditVariable
{
    private final Collection<VariableGroup> fAllGroups;

    private final String fKey;

    private String fDisplayName;
    
    private String fHelpText;
    
    private int fGroupId;
    
    /**
     * Constructs an instance.
     * @param variable
     * @param allGroups
     */
    public EditVariable(
            final AbstractVariable variable,
            final Collection<VariableGroup> allGroups) 
    {
        fKey = variable.getKey();
        fDisplayName = variable.getDisplayName();
        fHelpText = variable.getHelpText();
        fGroupId = variable.getGroup().getId();
        fAllGroups = allGroups;
    }

    public Collection<VariableGroup> getAllGroups()
    {
        return fAllGroups;
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
     * Returns the database ID of the variable's group.
     */
    public int getGroupId()
    {
        return fGroupId;
    }
    
    /**
     * Sets the database ID of the variable's group. Accepts an invalid group,
     * though {@link #applyToVariable(AbstractVariable)} will throw an exception
     * if an invalid group is set.
     */
    public void setGroupId(final int groupId)
    {
        fGroupId = groupId;
    }
    
    /**
     * Returns the actual VariableGroup object corresponding to the set group
     * ID.
     * @return an {@link Optional} containing the group if it exists
     */
    public Optional<VariableGroup> getGroup()
    {
        Optional<VariableGroup> foundGroup = Optional.absent();
        // There should not be more than 10 groups, so just iterate.
        for (final VariableGroup g : fAllGroups)
        {
            if (g.getId() == fGroupId)
            {
                foundGroup = Optional.of(g);
            }
        }
        
        return foundGroup;
    }

    /**
     * Sets all stored properties on the given Variable.
     * @param variable the variable to modify
     * @return the same Variable reference for convenience
     * @throws IllegalStateException if no group exists for the set group ID
     */
    public AbstractVariable applyToVariable(final AbstractVariable variable)
    {
        variable.setDisplayName(fDisplayName);
        variable.setHelpText(fHelpText);
        variable.setGroup(getGroup().get());
        return variable;
    }
}

package gov.va.med.srcalc.web.view;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import gov.va.med.srcalc.domain.model.*;

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
 * <li>Call {@link #applyToVariable()} to update the target variable with the
 * new properties.</li>
 * </ol>
 */
public final class EditVariable
{
    private final ImmutableList<VariableGroup> fAllGroups;

    private final AbstractVariable fTarget;

    private String fDisplayName;
    
    private Optional<String> fHelpText;
    
    private int fGroupId;
    
    private final SortedSet<RiskModel> fDependentModels;
    
    /**
     * Constructs an instance.
     * @param variable the target variable. Will be stored, but not modified
     * until calling {@link #applyToVariable()}.
     * @param allGroups all available VariableGroups for user selection. Will
     * make a defensive copy, preserving iteration order.
     */
    public EditVariable(
            final AbstractVariable variable,
            final Collection<VariableGroup> allGroups) 
    {
        fTarget = variable;
        fDisplayName = variable.getDisplayName();
        fHelpText = variable.getHelpText();
        fGroupId = variable.getGroup().getId();
        fAllGroups = ImmutableList.copyOf(allGroups);  // defensive copy
        fDependentModels = new TreeSet<>();
    }
    
    /**
     * Calculates and stores which, if any, of the given RiskModels depend on
     * the target variable.
     * @param allModels the models to check
     */
    public void calculateDependentModels(final Collection<RiskModel> allModels)
    {
        for (final RiskModel model : allModels)
        {
            if (model.getRequiredVariables().contains(fTarget))
            {
                fDependentModels.add(model);
            }
        }
    }
    
    /**
     * <p>If {@link #calculateDependentModels(Collection)} has been called,
     * returns the set of RiskModels that depend on this variable for the
     * user's reference. Otherwise, returns an empty set.</p>
     * @return a set sorted by the RiskModels' natural order
     */
    public SortedSet<RiskModel> getDependentModels()
    {
        return fDependentModels;
    }

    /**
     * Returns the collection of all VariableGroups provided to the constructor.
     * Iteration order is preserved.
     */
    public ImmutableCollection<VariableGroup> getAllGroups()
    {
        return fAllGroups;
    }

    /**
     * The key that uniquely identifies the variable, suitable for displaying
     * to an administrative user.
     */
    public String getKey()
    {
        // Just return the current key of the target since it's read-only.
        return fTarget.getKey();
    }

    /**
     * Returns the display name which {@link #applyToVariable()} will set.
     */
    public String getDisplayName()
    {
        return fDisplayName;
    }

    /**
     * Sets the display name which {@link #applyToVariable()} will set.
     */
    public void setDisplayName(String displayName)
    {
        fDisplayName = displayName;
    }
    
    /**
     * Returns the help text which {@link #applyToVariable()} will set. Since
     * this property is intended to be used directly in JSPs, an absent value
     * is represented by an empty string.
     */
    public String getHelpText()
    {
        return fHelpText.or("");
    }

    /**
     * Sets the help text which {@link #applyToVariable()} will set.
     * @param helpText may be null or empty, which will be translated to an
     * absent value
     */
    public void setHelpText(String helpText)
    {
        fHelpText = Optional.fromNullable(Strings.emptyToNull(helpText));
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
     * though {@link #applyToVariable()} will throw an exception if an invalid
     * group is set.
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
     * Sets all stored properties on the target variable.
     * @return the stored target variable for convenience
     * @throws IllegalStateException if no group exists for the set group ID
     */
    public AbstractVariable applyToVariable()
    {
        fTarget.setDisplayName(fDisplayName);
        fTarget.setHelpText(fHelpText);
        fTarget.setGroup(getGroup().get());
        return fTarget;
    }
}

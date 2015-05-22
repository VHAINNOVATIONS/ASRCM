package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.*;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.web.view.Views;

/**
 * A form backing object for creating a new or editing an existing
 * MultiSelectVariable.
 */
public class EditMultiSelectVariable extends EditVariable
{
    private MultiSelectVariable.DisplayType fDisplayType;
    
    private final ArrayList<String> fOptions;
    
    /**
     * Constructs an instance with default values for all properties.
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditMultiSelectVariable(final ModelInspectionService modelService)
    {
        super(modelService);
        fDisplayType = MultiSelectVariable.DisplayType.Radio;
        // Start with one blank value for the user to fill out.
        fOptions = Lists.newArrayList("");
    }
    
    @Override
    public String getTypeName()
    {
        return "Multi-Select";
    }
    
    /**
     * Returns {@link Views#NEW_MULTI_SELECT_VARIABLE}.
     */
    @Override
    public String getNewViewName()
    {
        return Views.NEW_MULTI_SELECT_VARIABLE;
    }
    
    /**
     * Returns all possible display types for user selection.
     * @return an immutable collection
     */
    public final ImmutableCollection<MultiSelectVariable.DisplayType> getAllDisplayTypes()
    {
        return ImmutableList.copyOf(MultiSelectVariable.DisplayType.values());
    }
    
    /**
     * Returns the DisplayType to set on the variable.
     * @see MultiSelectVariable#getDisplayType()
     */
    public final MultiSelectVariable.DisplayType getDisplayType()
    {
        return fDisplayType;
    }
    
    /**
     * Sets the DisplayType to set on the variable.
     * @see MultiSelectVariable#getDisplayType()
     */
    public final void setDisplayType(final MultiSelectVariable.DisplayType displayType)
    {
        fDisplayType = displayType;
    }
    
    /**
     * Returns the options to set on the variable. Note that {@link
     * MultiSelectVariable}s actually have collections of {@link MultiSelectOption},
     * but since those objects effectively only contain Strings, we represent
     * them as Strings here for easy use in views.
     * @see MultiSelectVariable#getOptions()
     * @return a mutable List
     */
    public final List<String> getOptions()
    {
        return fOptions;
    }
    
    /**
     * Returns the options as a List of {@link MultiSelectOption}, the type
     * needed to actually create the variable.
     * @return an ImmutableList
     */
    protected ImmutableList<MultiSelectOption> getMultiSelectOptions()
    {
        final ArrayList<MultiSelectOption> options = new ArrayList<>(fOptions.size());
        for (final String option : fOptions)
        {
            options.add(new MultiSelectOption(option));
        }
        return ImmutableList.copyOf(options);
    }

    @Override
    public MultiSelectVariable buildNew()
    {
        final MultiSelectVariable var = new MultiSelectVariable(
                getDisplayName(),
                getGroup().get(),
                fDisplayType,
                getMultiSelectOptions(),
                getKey());
        applyBaseProperties(var);
        return var;
    }
    
}

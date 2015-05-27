package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.BooleanVariable;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.web.view.Views;

/**
 * <p>A form backing object for creating a new or editing an existing
 * BooleanVariable.</p>
 */
public class EditBooleanVar extends EditVar
{
    /**
     * Constructs an instance with default values for all properties.
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditBooleanVar(final ModelInspectionService modelService)
    {
        super(modelService);
    }
    
    /**
     * Constructs an instance with the properties initialized to the given
     * variable.
     * @param variable the existing variable containing the initial properties.
     * Not stored.
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditBooleanVar(
            final BooleanVariable variable,
            final ModelInspectionService modelService)
    {
        super(variable, modelService);
    }
    
    @Override
    public String getTypeName()
    {
        return "Checkbox";
    }

    /**
     * Returns {@link Views#NEW_BOOLEAN_VARIABLE}.
     */
    @Override
    public String getNewViewName()
    {
        return Views.NEW_BOOLEAN_VARIABLE;
    }
    
    @Override
    public BooleanVariable buildNew()
    {
        final BooleanVariable var =
                new BooleanVariable(getDisplayName(), getGroup().get(), getKey());
        applyBaseProperties(var);
        return var;
    }
    
}

package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.BooleanVariable;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.web.view.Views;

/**
 * Encapsulates the operation to update an existing BooleanVariable.
 */
public class EditExistingBooleanVar extends EditBooleanVar implements EditExistingVar
{
    private final BooleanVariable fTarget;
    
    /**
     * Constructs an instance to edit the given variable.
     * @param target the variable to edit
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditExistingBooleanVar(
            final BooleanVariable target,
            final ModelInspectionService modelService)
    {
        super(target, modelService);
        fTarget = target;
    }

    @Override
    public BooleanVariable applyToVariable()
    {
        // BooleanVariables only have base properties.
        applyBaseProperties(fTarget);
        return fTarget;
    }
    
    @Override
    public BooleanVariable getTargetVariable()
    {
        return fTarget;
    }
    
    /**
     * Returns {@link Views#EDIT_BOOLEAN_VARIABLE}.
     */
    @Override
    public String getEditViewName()
    {
        return Views.EDIT_BOOLEAN_VARIABLE;
    }
    
}

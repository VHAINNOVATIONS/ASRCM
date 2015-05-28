package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.web.view.Views;

/**
 * Encapsulates the operation to update an existing {@link MultiSelectVariable}.
 */
public class EditExistingMultiSelectVar extends EditMultiSelectVar implements EditExistingVar
{
    private final MultiSelectVariable fTarget;
    
    /**
     * Constructs an instance to edit the given variable.
     * @param target the variable to edit
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditExistingMultiSelectVar(
            final MultiSelectVariable target,
            final ModelInspectionService modelService)
    {
        super(target, modelService);
        
        fTarget = target;
    }

    @Override
    public AbstractVariable getTargetVariable()
    {
        return fTarget;
    }
    
    @Override
    public String getEditViewName()
    {
        return Views.EDIT_MULTI_SELECT_VARIABLE;
    }
    
    @Override
    public MultiSelectVariable applyToVariable()
    {
        applyBaseProperties(fTarget);
        fTarget.setDisplayType(getDisplayType());
        fTarget.getOptions().clear();
        fTarget.getOptions().addAll(getMultiSelectOptions());
        return fTarget;
    }
    
}

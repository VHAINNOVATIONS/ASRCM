package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.web.view.Views;

/**
 * <p>Encapsulates the operation update an existing {@link DiscreteNumericalVariable}.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class EditExistingDiscreteNumericalVar
        extends EditDiscreteNumericalVar implements EditExistingVar
{
    private final DiscreteNumericalVariable fTarget;
    
    public EditExistingDiscreteNumericalVar(
            final DiscreteNumericalVariable target,
            final ModelInspectionService modelService)
    {
        super(target, modelService);
        fTarget = target;
    }
    
    @Override
    public DiscreteNumericalVariable getTargetVariable()
    {
        return fTarget;
    }
    
    @Override
    public String getEditViewName()
    {
        return Views.EDIT_DISCRETE_NUMERICAL_VARIABLE;
    }
    
    @Override
    public DiscreteNumericalVariable applyToVariable()
    {
        applyNumericalProperties(fTarget);
        fTarget.getCategories().clear();
        fTarget.getCategories().addAll(buildCategories());
        return fTarget;
    }
    
}

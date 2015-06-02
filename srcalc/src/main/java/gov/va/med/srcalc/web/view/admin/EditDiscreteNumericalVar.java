package gov.va.med.srcalc.web.view.admin;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.web.view.Views;

/**
 * <p>A form backing object for creating a new or editing an existing
 * {@link DiscreteNumericalVariable}.</p>
 * 
 * <p>This code is tightly coupled with newDiscreteNumericalVariable.jsp.</p>
 */
public class EditDiscreteNumericalVar extends EditAbstractNumericalVar
{
    /**
     * Constructs an instance with default values for all properties.
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditDiscreteNumericalVar(final ModelInspectionService modelService)
    {
        super(modelService);
    }
    
    @Override
    public String getTypeName()
    {
        return "Discrete Numerical";
    }
    
    @Override
    public EditDiscreteNumericalVarValidator getValidator()
    {
        return new EditDiscreteNumericalVarValidator();
    }
    
    @Override
    public String getNewViewName()
    {
        return Views.NEW_DISCRETE_NUMERICAL_VARIABLE;
    }

    @Override
    public DiscreteNumericalVariable buildNew()
    {
        // TODO: Build the set of Categories.
        final ImmutableSet<DiscreteNumericalVariable.Category> categories = ImmutableSet.of();

        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                getDisplayName(), getGroup().get(), categories, getKey());
        applyBaseProperties(var);
        var.setUnits(getUnits());
        var.setValidRange(getValidRange().build());
        return var;
    }
}

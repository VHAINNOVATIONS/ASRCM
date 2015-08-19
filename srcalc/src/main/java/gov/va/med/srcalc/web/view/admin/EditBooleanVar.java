package gov.va.med.srcalc.web.view.admin;

import com.google.common.collect.ImmutableSortedSet;

import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.model.BooleanVariable;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.web.view.Views;

/**
 * <p>A form backing object for creating a new or editing an existing
 * BooleanVariable.</p>
 */
public class EditBooleanVar extends EditBaseVar
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
     * The properties are copied but this object is not stored.
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
    
    @Override
    public EditBaseVarValidator getValidator()
    {
        // Boolean variables only have common properties: just use the
        // EditBaseVarValidator.
        return new EditBaseVarValidator();
    }
    
    /**
     * Returns an empty set. There are no retrievers for boolean variables.
     */
    @Override
    public ImmutableSortedSet<ValueRetriever> getAllRetrievers()
    {
        return ImmutableSortedSet.copyOf(ValueRetriever.BOOLEAN_SET);
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

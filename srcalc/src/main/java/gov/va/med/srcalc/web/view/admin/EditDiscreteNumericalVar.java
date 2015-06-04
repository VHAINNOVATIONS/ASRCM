package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.web.view.Views;

/**
 * <p>A form backing object for creating a new or editing an existing
 * {@link DiscreteNumericalVariable}.</p>
 * 
 * <p>This code is tightly coupled with newDiscreteNumericalVariable.jsp.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class EditDiscreteNumericalVar extends EditAbstractNumericalVar
{
    private static final int MIN_CATEGORIES = 2;
    
    private static final int MAX_CATEGORIES = 10;
    
    private final ArrayList<CategoryBuilder> fCategories;
    
    /**
     * Constructs an instance with default values for all properties. The
     * default list of category builders has 2 default builders (to offer the
     * user initial categories to fill out).
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditDiscreteNumericalVar(final ModelInspectionService modelService)
    {
        super(modelService);
        fCategories = Lists.newArrayList(
                new CategoryBuilder(),
                new CategoryBuilder());
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
    
    /**
     * Returns the modifiable list of CategoryBuilders.
     */
    public List<CategoryBuilder> getCategories()
    {
        return fCategories;
    }
    
    /**
     * Returns the minimum supported number of categories, {@value #MIN_CATEGORIES}.
     */
    public int getMinCategories()
    {
        return MIN_CATEGORIES;
    }
    
    /**
     * Returns the maximum supported number of categories, {@value #MAX_CATEGORIES}.
     */
    public int getMaxCategories()
    {
        return MAX_CATEGORIES;
    }
    
    @Override
    public String getNewViewName()
    {
        return Views.NEW_DISCRETE_NUMERICAL_VARIABLE;
    }

    @Override
    public DiscreteNumericalVariable buildNew()
    {
        Preconditions.checkState(
                fCategories.size() >= MIN_CATEGORIES,
                "at least %s categories are required, but only %s provided",
                MIN_CATEGORIES, fCategories.size());

        final ImmutableSet.Builder<DiscreteNumericalVariable.Category> categories =
                ImmutableSet.builder();
        for (final CategoryBuilder catBuilder : fCategories)
        {
            categories.add(catBuilder.build());
        }

        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                getDisplayName(), getGroup().get(), categories.build(), getKey());
        applyBaseProperties(var);
        var.setUnits(getUnits());
        var.setValidRange(getValidRange().build());
        return var;
    }
}

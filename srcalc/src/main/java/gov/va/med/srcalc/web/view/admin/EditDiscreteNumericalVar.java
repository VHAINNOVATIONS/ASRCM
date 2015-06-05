package gov.va.med.srcalc.web.view.admin;

import java.util.*;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.*;

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
    
    /**
     * Constructs an instance with the properties initialized to the given prototype
     * variable.
     * @param prototype the existing variable containing the initial properties.
     * The properties are copied but this object is not stored.
     * @param modelService to provide reference data (e.g., available
     * VariableGroups) to the user
     */
    public EditDiscreteNumericalVar(
            final DiscreteNumericalVariable prototype,
            final ModelInspectionService modelService)
    {
        super(prototype, modelService);
        
        // Copy the categories.
        fCategories = new ArrayList<>(prototype.getCategories().size());
        for (final DiscreteNumericalVariable.Category cat : prototype.getCategories())
        {
            fCategories.add(CategoryBuilder.fromPrototype(cat));
        }
    }
    
    @Override
    public final String getTypeName()
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
    public final List<CategoryBuilder> getCategories()
    {
        return fCategories;
    }
    
    /**
     * <p>Like {@link #getCategories()} but with trailing blank categories trimmed off. A
     * Category is considered blank if its value is blank. (The user still may have
     * inputted an upper bound.)</p>
     * 
     * <p>This method is useful because we allow the user to delete trailing categories
     * by clearing out the category names.</p>
     * 
     * @return an ImmutableList
     */
    public final ImmutableList<CategoryBuilder> getTrimmedCategories()
    {
        // Start with a copy of all the categories.
        final ArrayList<CategoryBuilder> trimmed = new ArrayList<>(fCategories);
        
        // Iterate from the end, removing as we go.
        for (int i = fCategories.size() - 1; i >= 0; --i)
        {
            if (Strings.isNullOrEmpty(trimmed.get(i).getValue()))
            {
                trimmed.remove(i);
            }
            // Break at the first non-empty String.
            else
            {
                break;
            }
        }
        
        return ImmutableList.copyOf(trimmed);
    }
    
    /**
     * Returns the categories as a Set of {@link DiscreteNumericalVariable.Category},
     * the type needed to actually create the variable. Internally uses
     * @return an ImmutableSet
     */
    protected final ImmutableSet<DiscreteNumericalVariable.Category> buildCategories()
    {
        final ImmutableList<CategoryBuilder> trimmed = getTrimmedCategories();
        final HashSet<DiscreteNumericalVariable.Category> categories =
                new HashSet<>(trimmed.size());
        for (final CategoryBuilder cat : trimmed)
        {
            categories.add(cat.build());
        }
        return ImmutableSet.copyOf(categories);
    }
    
    /**
     * Returns the minimum supported number of categories, {@value #MIN_CATEGORIES}.
     */
    public final int getMinCategories()
    {
        return MIN_CATEGORIES;
    }
    
    /**
     * Returns the maximum supported number of categories, {@value #MAX_CATEGORIES}.
     */
    public final int getMaxCategories()
    {
        return MAX_CATEGORIES;
    }
    
    @Override
    public final String getNewViewName()
    {
        return Views.NEW_DISCRETE_NUMERICAL_VARIABLE;
    }

    @Override
    public final DiscreteNumericalVariable buildNew()
    {
        Preconditions.checkState(
                fCategories.size() >= MIN_CATEGORIES,
                "at least %s categories are required, but only %s provided",
                MIN_CATEGORIES, fCategories.size());

        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                getDisplayName(), getGroup().get(), buildCategories(), getKey());
        applyNumericalProperties(var);
        return var;
    }
}

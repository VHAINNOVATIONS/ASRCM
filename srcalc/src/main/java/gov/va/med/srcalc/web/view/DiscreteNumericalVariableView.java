package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;

import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;

/**
 * This class represents the visible attributes of a {@link DiscreteNumericalVariable} in order to
 * provide other attributes that are only necessary for display.
 */
public class DiscreteNumericalVariableView extends VariableView
{
    private static final String DISCRETE_NUMERICAL_FRAGMENT = "discreteNumericalFragment.jsp";
    
    private final String fUnits;
    private final SortedSet<Category> fCategories;
    
    protected DiscreteNumericalVariableView()
    {
        fUnits = "";
        fCategories = new TreeSet<Category>();
    }
    
    /**
     * Constructs an instance.
     * @param variable the DiscreteNumericalVariable to copy properties from
     * @param referenceInfo the reference information for this DiscreteNumericalVariable
     */
    public DiscreteNumericalVariableView(final DiscreteNumericalVariable variable, final String referenceInfo)
    {
        super(variable.getDisplayName(), variable.getGroup(), variable.getKey(),
                variable.getHelpText(), Optional.of(referenceInfo), DISCRETE_NUMERICAL_FRAGMENT);
        fUnits = variable.getUnits();
        fCategories = ImmutableSortedSet.copyOf(variable.getCategoriesWnlFirst());
    }

    /**
     * The units (if any) for the numerical portion of this object. May be an empty string if 
     * units are not applicable, but will never be null.
     */
    public String getUnits()
    {
        return fUnits;
    }
    
    /**
     * Returns the SortedSet that contains the categories (options) for this object.
     */
    public SortedSet<Category> getCategories()
    {
        return fCategories;
    }
}

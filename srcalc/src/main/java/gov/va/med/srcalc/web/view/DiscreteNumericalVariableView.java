package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;

import java.util.List;
import java.util.SortedSet;

import com.google.common.collect.ImmutableSortedSet;

/**
 * This class represents the visible attributes of a {@link DiscreteNumericalVariable} in order to
 * provide other attributes that are only necessary for display.
 */
public class DiscreteNumericalVariableView extends VariableView
{
    private static final String DISCRETE_NUMERICAL_FRAGMENT = "discreteNumericalInputs.jsp";
    
    private final String fUnits;
    private final SortedSet<Category> fCategories;
    
    /**
     * Constructs an instance.
     * @param variable the DiscreteNumericalVariable to copy properties from
     * @param referenceInfo the reference information for this DiscreteNumericalVariable
     */
    public DiscreteNumericalVariableView(final DiscreteNumericalVariable variable, final List<String> referenceInfo)
    {
        super(variable, referenceInfo);
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
    
    /**
     * Uses {@link VariableEntry#makeDynamicValuePath(String)} for a DiscreteNumericalVariable's key.
     * Uses {@link VariableEntry#makeNumericalInputName(String)} to produce the necessary name.
     */
    public String getNumericalVarPath()
    {
        return VariableEntry.makeDynamicValuePath(VariableEntry.makeNumericalInputName(this.getKey()));
    }

    @Override
    public String getFragmentName()
    {
        return DISCRETE_NUMERICAL_FRAGMENT;
    }
}

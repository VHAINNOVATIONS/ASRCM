package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;

import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;

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
    
    public DiscreteNumericalVariableView(final DiscreteNumericalVariable variable, final String referenceInfo)
    {
        super(variable.getDisplayName(), variable.getGroup(), variable.getKey(),
                variable.getHelpText(), Optional.of(referenceInfo), DISCRETE_NUMERICAL_FRAGMENT);
        fUnits = variable.getUnits();
        fCategories = ImmutableSortedSet.copyOf(variable.getCategoriesWnlFirst());
    }

    public String getUnits()
    {
        return fUnits;
    }
    
    public SortedSet<Category> getCategories()
    {
        return fCategories;
    }
}

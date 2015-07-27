package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;

import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;

public class DiscreteNumericalVariableView extends VariableView
{
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
                variable.getHelpText(), Optional.of(referenceInfo));
        fUnits = variable.getUnits();
        fCategories = ImmutableSortedSet.copyOf(variable.getCategories());
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

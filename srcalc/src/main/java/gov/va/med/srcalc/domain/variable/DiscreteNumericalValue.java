package gov.va.med.srcalc.domain.variable;

import gov.va.med.srcalc.domain.variable.DiscreteNumericalVariable.Category;

public class DiscreteNumericalValue implements Value
{
    private final DiscreteNumericalVariable fVariable;
    private final Category fSelectedCategory;
    
    public DiscreteNumericalValue(
            final DiscreteNumericalVariable variable, final Category selectedCategory)
    {
        fVariable = variable;
        fSelectedCategory = selectedCategory;
    }

    @Override
    public DiscreteNumericalVariable getVariable()
    {
        return fVariable;
    }

    @Override
    public Category getValue()
    {
        return fSelectedCategory;
    }

    @Override
    public String getDisplayString()
    {
        // If a numerical value was given, write the category with the actual
        // value. Otherwise, just write "Presumed" followed by the category.
        return "Presumed " + fSelectedCategory.getOption().getValue();
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %s", getVariable(), getValue());
    }
}

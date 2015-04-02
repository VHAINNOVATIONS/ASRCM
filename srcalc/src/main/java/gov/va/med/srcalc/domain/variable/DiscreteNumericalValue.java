package gov.va.med.srcalc.domain.variable;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.domain.variable.DiscreteNumericalVariable.Category;

public class DiscreteNumericalValue implements DiscreteValue
{
    private final DiscreteNumericalVariable fVariable;
    private final Category fSelectedCategory;
    private final float fNumericalValue;
    
    protected DiscreteNumericalValue(
            final DiscreteNumericalVariable variable,
            final Category selectedCategory,
            final float numericalValue)
    {
        fVariable = variable;
        fSelectedCategory = selectedCategory;
        fNumericalValue = numericalValue;
    }
    
    public static DiscreteNumericalValue fromCategory(
            final DiscreteNumericalVariable variable, final Category selectedCategory)
    {
        return new DiscreteNumericalValue(variable, selectedCategory, Float.NaN);
    }
    
    /**
     * Constructs an instance from the given actual numerical value.
     * @param variable
     * @param numericalValue
     * @throws ValueTooHighException 
     * @throws ValueTooLowException 
     * @throws ConfigurationException if the value is not in any of the
     * categories but is within the valid range
     */
    public static DiscreteNumericalValue fromNumerical(
            final DiscreteNumericalVariable variable, final float numericalValue)
                    throws ValueTooLowException, ValueTooHighException
    {
        // Ensure the value is in within range.
        variable.checkValue(numericalValue);

        final Category category = variable.getContainingCategory(numericalValue);
        if (category == null)
        {
            throw new ConfigurationException("No matching range for valid numerical value");
        }
        return new DiscreteNumericalValue(variable, category, numericalValue);
    }

    @Override
    public DiscreteNumericalVariable getVariable()
    {
        return fVariable;
    }
    
    @Override
    public MultiSelectOption getSelectedOption()
    {
        return fSelectedCategory.getOption();
    }

    @Override
    public Category getValue()
    {
        return fSelectedCategory;
    }
    
    /**
     * Returns the actual numerical value if one was provided. Otherwise returns
     * {@link Float#NaN}.
     */
    public float getNumericalValue()
    {
        return fNumericalValue;
    }

    @Override
    public String getDisplayString()
    {
        // If a numerical value was given, write the category with the actual
        // value. Otherwise, just write "Presumed" followed by the category.
        if (Float.isNaN(fNumericalValue))
        {
            return "Presumed " + fSelectedCategory.getOption().getValue();
        }
        else
        {
            return String.format(
                    "%s (Actual Value: %s)",
                    fSelectedCategory.getOption().getValue(),
                    Float.toString(fNumericalValue));
        }
    }
    
    @Override
	public void accept(final ValueVisitor valueVisitor)
    {
		valueVisitor.visitDiscreteNumerical(this);
	}
    
    @Override
    public String toString()
    {
        return String.format("%s = %s", getVariable(), getValue());
    }
}

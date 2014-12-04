package gov.va.med.srcalc.domain.variable;

public class NumericalValue implements Value
{
    private final NumericalVariable fVariable;
    private final float fValue;
    
    /**
     * Constructs an instance for the given {@link NumericalVariable} with the
     * given value.
     * @throws ValueTooLowException if the value is below the variable's minimum
     * @throws ValueTooHighException if the value is above the variable's minimum
     */
    public NumericalValue(final NumericalVariable variable, final float value)
            throws ValueTooLowException, ValueTooHighException
    {
        fVariable = variable;
        fValue = checkValue(value);
    }
    
    /**
     * If the value is valid for the configured {@link NumericalVariable},
     * returns the value. Otherwise, throws an {@link InvalidValueException}.
     * @return the valid value
     * @throws ValueTooLowException if the value is below the minimum
     * @throws ValueTooHighException if the value is above the minimum
     */
    private float checkValue(final float value)
            throws ValueTooLowException, ValueTooHighException
    {
        if (value < fVariable.getMinValue())
        {
            throw new ValueTooLowException(
                    "value must be greater than or equal to " + fVariable.getMinValue());
        }
        else if (value > fVariable.getMaxValue())
        {
            throw new ValueTooHighException(
                    "value must be less than or equal to " + fVariable.getMaxValue());
        }
        return value;
    }
    
    @Override
    public NumericalVariable getVariable()
    {
        return fVariable;
    }
    
    @Override
    public Float getValue()
    {
        return fValue;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %d", getVariable(), getValue());
    }
}

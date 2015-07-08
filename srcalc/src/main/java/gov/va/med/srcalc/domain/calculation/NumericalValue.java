package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.model.*;


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
        fValue = fVariable.getValidRange().checkValue(value);
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
    
    /**
     * Returns the floating point value in the format used by {@link Float#toString(float)}
     * to display the minimal required floating part digits.
     */
    @Override
    public String getDisplayString()
    {
        return getValue().toString();
    }
    
    @Override
    public void accept(final ValueVisitor visitor)
    {
        visitor.visitNumerical(this);
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %f", getVariable(), getValue());
    }
}

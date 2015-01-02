package gov.va.med.srcalc.domain.variable;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * A Variable with numerical (floating-point) value.
 */
@Entity
public class NumericalVariable extends Variable
{
    private float fMinValue = 0.0f;
    private float fMaxValue = Float.POSITIVE_INFINITY;
    
    /**
     * For reflection-based construction only. Business code should use
     * {@link #NumericalVariable(String, VariableGroup)}.
     */
    NumericalVariable()
    {
    }
    
    public NumericalVariable(final String displayName, final VariableGroup group)
    {
        super(displayName, group);
    }

    /**
     * The minimum allowed value for this variable. Default 0.
     */
    @Basic
    public float getMinValue()
    {
        return fMinValue;
    }

    public void setMinValue(final float minValue)
    {
        this.fMinValue = minValue;
    }

    /**
     * The maximum allowed value for this variable. Default
     * {@link Float#POSITIVE_INFINITY}.
     */
    @Basic
    public float getMaxValue()
    {
        return fMaxValue;
    }

    public void setMaxValue(final float maxValue)
    {
        this.fMaxValue = maxValue;
    }
    
    /**
     * Checks the given value against the configured minimum and maximum bounds.
     * If the value is valid, returns the value. Otherwise, throws an {@link
     * InvalidValueException}.
     * @return the valid value for convenience
     * @throws ValueTooLowException if the value is below the minimum
     * @throws ValueTooHighException if the value is above the minimum
     */
    public float checkValue(final float value)
            throws ValueTooLowException, ValueTooHighException
    {
        if (value < getMinValue())
        {
            throw new ValueTooLowException(
                    "value must be greater than or equal to " + getMinValue());
        }
        else if (value > getMaxValue())
        {
            throw new ValueTooHighException(
                    "value must be less than or equal to " + getMaxValue());
        }
        return value;
    }
    
    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitNumerical(this);
    }
}

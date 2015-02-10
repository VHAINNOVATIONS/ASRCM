package gov.va.med.srcalc.domain.variable;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractNumericalVariable extends AbstractVariable
{
    /**
     * The maximum length of the units string.
     */
    public static final int UNITS_MAX = 40;
    
    private float fMinValue = 0.0f;
    private float fMaxValue = Float.POSITIVE_INFINITY;
    private String fUnits = "";

    /**
     * For reflection-based construction only. Business code should use
     * {@link #NumericalVariable(String, VariableGroup)}.
     */
    AbstractNumericalVariable()
    {
    }
    
    public AbstractNumericalVariable(final String displayName, final VariableGroup group, final String key)
    {
        super(displayName, group, key);
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
     * The units (if any) for the number. May be an empty string if units are
     * not applicable, but will never be null.
     * @return
     */
    @Basic
    @Column(nullable = false, length = UNITS_MAX)
    public String getUnits()
    {
        return fUnits;
    }

    /**
     * Sets the units.
     * @param units must not be null
     * @throws NullPointerException if the given value is null
     * @throws IllegalArgumentException if the given value is over 40 characters
     */
    public void setUnits(final String units)
    {
        // Note: will throw an NPE if the argument is null
        if (units.length() > UNITS_MAX)
        {
            throw new IllegalArgumentException("The units must be 40 characters or less.");
        }
        fUnits = units;
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
}

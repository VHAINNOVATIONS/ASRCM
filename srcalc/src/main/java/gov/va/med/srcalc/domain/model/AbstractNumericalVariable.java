package gov.va.med.srcalc.domain.model;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractNumericalVariable extends AbstractVariable
{
    /**
     * The maximum length of the units string.
     */
    public static final int UNITS_MAX = 40;
    
    private NumericalRange fRange = new NumericalRange(0.0f, true, Float.POSITIVE_INFINITY, true);

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
    public float getMinValue()
    {
        return fRange.getLowerBound();
    }

    public void setMinValue(final float minValue)
    {
        this.fRange.setLowerBound(minValue);
    }
    
    public boolean getMinInclusive()
    {
    	return fRange.isLowerInclusive();
    }

    public void setMinInclusive(final boolean inclusive)
    {
    	this.fRange.setLowerInclusive(inclusive);
    }
    
    public boolean getMaxInclusive()
    {
    	return fRange.isUpperInclusive();
    }

    public void setMaxInclusive(final boolean inclusive)
    {
    	this.fRange.setUpperInclusive(inclusive);
    }
    
    /**
     * The maximum allowed value for this variable. Default
     * {@link Float#POSITIVE_INFINITY}.
     */
    public float getMaxValue()
    {
        return fRange.getUpperBound();
    }

    public void setMaxValue(final float maxValue)
    {
        this.fRange.setUpperBound(maxValue);
    }
    
    /**
     * The units (if any) for the number. May be an empty string if units are
     * not applicable, but will never be null.
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
    	if(fRange.isLowerInclusive())
    	{
    		if(value < getMinValue())
    		{
    			throw new ValueTooLowException(ValueTooLowException.ERROR_CODE_INCLUSIVE,
                    "value must be greater than or equal to " + getMinValue());
    		}
    	}
    	else
    	{
    		if (value <= getMinValue())
    		{
    			throw new ValueTooLowException(ValueTooLowException.ERROR_CODE_EXCLUSIVE,
                    "value must be greater than " + getMinValue());
    		}
    	}
        if (fRange.isUpperInclusive())
        {
        	if(value > getMaxValue())
        	{
        		throw new ValueTooHighException(ValueTooHighException.ERROR_CODE_INCLUSIVE,
                    "value must be less than or equal to " + getMaxValue());
        	}
        }
        else
        {
        	if(value >= getMaxValue())
        	{
        		throw new ValueTooHighException(ValueTooHighException.ERROR_CODE_EXCLUSIVE,
                    "value must be less than " + getMaxValue());
        	}
        }
        return value;
    }
}

package gov.va.med.srcalc.util;

import java.util.Date;

/**
 * A class that will store the value of a retrieved field as well as the
 * date on which that field was measured. The value and date should not change
 * after they are stored. The class currently only supports the double type.
 */
public class RetrievedValue
{   
    private final double fValue;
    private final Date fMeasureDate;
    private final String fUnits;
    
    /**
     * @param value The amount of the measurement.
     * @param measureDate The date on which the measurement was made.
     * @param units A string delineating the units.
     */
    public RetrievedValue(final double value, final Date measureDate, final String units)
    {
        fValue = value;
        fMeasureDate = measureDate;
        fUnits = units;
    }
    
    /**
     * Return the value for this retrieved field.
     */
    public double getValue()
    {
        return fValue;
    }
    
    /**
     * Return the date on which this value was measured.
     */
    public Date getMeasureDate()
    {
        return fMeasureDate;
    }
    
    /**
     * Return the units associated with this value.
     */
    public String getUnits()
    {
        return fUnits;
    }
}

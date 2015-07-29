package gov.va.med.srcalc.domain;

import java.util.Objects;

import org.joda.time.DateTime;

/**
 * A class that will store the name of a health factor along with the 
 * date on which that factor was noted. Instances of this class are immutable.
 */
public class HealthFactor
{
    private final DateTime fDate;
    private final String fName;
    
    public HealthFactor(final DateTime date, final String factor)
    {
        fDate = date;
        fName = factor;
    }
    
    /**
     * Returns the date on which this health factor was noted.
     */
    public DateTime getDate()
    {
        return fDate;
    }
    
    /**
     * Returns the name of the health factor.
     */
    public String getName()
    {
        return fName;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s %s", fDate, fName);
    }
    
    @Override
    public boolean equals(final Object obj)
    {
        if(obj instanceof HealthFactor)
        {
            final HealthFactor other = (HealthFactor) obj;
            return Objects.equals(this.fDate, other.fDate) &&
                    Objects.equals(this.fName, other.fName);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(this.fDate, this.fName);
    }
}

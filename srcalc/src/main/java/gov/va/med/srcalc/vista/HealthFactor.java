package gov.va.med.srcalc.vista;

import java.util.Date;
import java.util.Objects;

/**
 * A class that will store the name of a health factor along with the 
 * date on which that factor was noted. Instances of this class are immutable.
 */
public class HealthFactor
{
    private final Date fDate;
    private final String fFactor;
    
    public HealthFactor(final Date date, final String factor)
    {
        fDate = date;
        fFactor = factor;
    }
    
    /**
     * Returns the date on which this health factor was noted.
     */
    public Date getDate()
    {
        return fDate;
    }
    
    /**
     * Returns the name of the health factor.
     */
    public String getFactor()
    {
        return fFactor;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s %s", fDate, fFactor);
    }
    
    @Override
    public boolean equals(final Object obj)
    {
        if(obj instanceof HealthFactor)
        {
            final HealthFactor other = (HealthFactor) obj;
            return Objects.equals(this.fDate, other.fDate) &&
                    Objects.equals(this.fFactor, other.fFactor);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(this.fDate, this.fFactor);
    }
}

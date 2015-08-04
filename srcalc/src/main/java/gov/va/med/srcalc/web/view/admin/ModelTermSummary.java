package gov.va.med.srcalc.web.view.admin;

import java.util.Objects;

/**
 * <p>Summarizes a model term for display to the user.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class ModelTermSummary
{
    private final String fIdentificationString;
    
    private final String fTermType;
    
    private final float fCoefficient;
    
    public ModelTermSummary(
            final String identificationString,
            final String termType,
            final float coefficient)
    {
        fIdentificationString = identificationString;
        fTermType = termType;
        fCoefficient = coefficient;
    }
    
    /**
     * A string identifying the term to a user, such as "ASA Classification = Class 5".
     */
    public String getIdentificationString()
    {
        return fIdentificationString;
    }
    
    /**
     * A string identifying the type of term to a user, such as "Multi-Select".
     */
    public String getTermType()
    {
        return fTermType;
    }
    
    /**
     * The term's coefficient.
     */
    public float getCoefficient()
    {
        return fCoefficient;
    }
    
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof ModelTermSummary)
        {
            final ModelTermSummary other = (ModelTermSummary)o;
            return Objects.equals(this.fIdentificationString, other.fIdentificationString) &&
                    Objects.equals(this.fTermType, other.fTermType) &&
                    (Float.compare(this.fCoefficient, other.fCoefficient) == 0);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(fIdentificationString, fTermType, fCoefficient);
    }
    
    @Override
    public String toString()
    {
        return String.format(
                // Use a more compact format than we would get from
                // MoreObjects.toStringHelper().
                "ModelTermSummary[%s,%s,%s]",
                fIdentificationString, fTermType, fCoefficient);
    }
}

package gov.va.med.srcalc.domain.model;

import java.util.Objects;

import javax.persistence.*;

/**
 * <p>Represents a medical procedure, particuarly a CPT code.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
@Table(name="CPT")   // call it CPT because "PROCEDURE" is a SQL reserved word
public final class Procedure
{
    private int fId;
    
    private String fCptCode;
    
    private float fRvu;
    
    private String fShortDescription;
    
    private String fLongDescription;
    
    private String fComplexity;
    
    private boolean fEligible;

    /**
     * Package-private default constructor mainly for Hibernate use. Be careful
     * about using this constructor as the CPT Code must be set for the object
     * to be in a valid state.
     */
    Procedure()
    {
        fCptCode = "NOT SET";
    }
    
    /**
     * Constructs an instance with the given attributes.
     */
    public Procedure(
            final String cptCode,
            final float rvu,
            final String shortDescription,
            final String longDescription,
            final String complexity)
    {
        fCptCode = cptCode;
        fRvu = rvu;
        fShortDescription = shortDescription;
        fLongDescription = longDescription;
        fComplexity = complexity;
        fEligible = true;
    }

    @Id // We use method-based property detection throughout the app.
    @GeneratedValue
    public int getId()
    {
        return fId;
    }

    /**
     * For reflection-based construction only.
     */
    void setId(final int id)
    {
        this.fId = id;
    }

    @Basic
    public String getCptCode()
    {
        return fCptCode;
    }

    /**
     * For reflection-based construction only. The CPT code is this object's
     * natural identifier and should not be changed. If a Procedure needs a new
     * CPT code, the existing Procedure should be deactivated and a new one
     * created.
     */
    void setCptCode(final String cptCode)
    {
        this.fCptCode = cptCode;
    }

    /**
     * Relative Value Unit.
     */
    @Basic
    public float getRvu()
    {
        return fRvu;
    }

    public void setRvu(final float rvu)
    {
        this.fRvu = rvu;
    }

    @Basic
    public String getShortDescription()
    {
    	return fShortDescription;
    }

    public void setShortDescription(final String description)
    {
    	fShortDescription = description;
    }
    
    @Basic
    public String getLongDescription()
    {
        return fLongDescription;
    }

    public void setLongDescription(final String longDescription)
    {
        fLongDescription = longDescription;
    }
    
    @Basic
    public String getComplexity()
    {
    	return fComplexity;
    }
    
    public void setComplexity(final String complexity)
    {
    	fComplexity = complexity;
    }

    /**
     * Returns whether this procedure is eligible for a risk calculation.
     */
    @Basic
    public boolean isEligible()
    {
        return fEligible;
    }

    /**
     * Sets whether this procedure is eligible for a risk calculation.
     */
    public void setEligible(final boolean eligible)
    {
        fEligible = eligible;
    }

    @Override
    public String toString()
    {
        return String.format("%s - %s (%s)",
                // Use Float.toString() to get only as many fractional part
                // digits as we need (e.g., 10.0 or 5.56).
                getCptCode(), getShortDescription(), Float.toString(getRvu()));
    }
    
    /**
     * Like {@link #toString()} but uses the long instead of short description.
     * Sometimes the long description is too long to be appropriate for toString().
     * @return a possibly-long String (we do not limit the length of the long
     * description)
     */
    @Transient
    public String getLongString()
    {
        return String.format("%s - %s (%s)",
                getCptCode(), getLongDescription(), Float.toString(getRvu()));
    }
    
    /**
     * Compares procedures based on CPT code and RVU.
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof Procedure)  // false if o == null
        {
            final Procedure other = (Procedure)o;
            
            // Compare "business equality", not using the primary key, per
            // Hibernate recommendation.
            //
            // Use Float.compare to handle NaN properly.
            return (Float.compare(getRvu(), other.getRvu()) == 0) &&
                    Objects.equals(getCptCode(), other.getCptCode());
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getRvu(), getCptCode());
    }
}


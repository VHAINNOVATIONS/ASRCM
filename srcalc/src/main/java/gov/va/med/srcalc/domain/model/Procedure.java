package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.util.Preconditions;

import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.Immutable;

/**
 * <p>Represents a medical procedure, particuarly a CPT code. Presents an immutable public
 * interface.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
@Table(name="CPT")   // call it CPT because "PROCEDURE" is a SQL reserved word
@Immutable
public final class Procedure
{
    /**
     * The length of all CPT codes, {@value}.
     */
    public static final int CPT_CODE_LENGTH = 5;
    
    /**
     * The maximum length of both the long and short description, {@value}. See
     * {@link #getShortDescription() the short description comment} for why this is the
     * same for both short and long.
     */
    public static final int DESCRIPTION_MAX = 256;
    
    /**
     * The maximum length of the complexity string, {@value}.
     */
    public static final int COMPLEXITY_MAX = 40;
    
    private int fId;
    
    private String fCptCode;
    
    private float fRvu;
    
    private String fShortDescription;
    
    private String fLongDescription;
    
    private String fComplexity;
    
    private boolean fEligible;

    /**
     * Package-private default constructor intended only for reflection-based
     * construction.
     */
    Procedure()
    {
        // Set sentinel values.
        fCptCode = "NOT SET";
        fRvu = Float.NaN;
        fShortDescription = "NOT SET";
        fLongDescription = "NOT SET";
        fComplexity = "NOT SET";
        fEligible = false;
    }
    
    /**
     * Constructs an instance with the given attributes.
     */
    public Procedure(
            final String cptCode,
            final float rvu,
            final String shortDescription,
            final String longDescription,
            final String complexity,
            final boolean eligible)
    {
        // Use setters to verify constraints.
        setCptCode(cptCode);
        setRvu(rvu);
        setShortDescription(shortDescription);
        setLongDescription(longDescription);
        setComplexity(complexity);
        setEligible(eligible);
    }

    /**
     * The unique id of this procedure (not the CPT code).
     */
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

    /**
     * Returns the Procedure's CPT code.
     * @return a 5-character String
     */
    @Basic
    @Column(nullable = false, length = CPT_CODE_LENGTH)
    public String getCptCode()
    {
        return fCptCode;
    }

    /**
     * Sets the CPT code. For reflection-based construction only.
     * @throws IllegalArgumentException if the given code is not 5 characters long
     */
    void setCptCode(final String cptCode)
    {
        this.fCptCode = Preconditions.requireWithin(
                cptCode, CPT_CODE_LENGTH, CPT_CODE_LENGTH);
    }

    /**
     * Relative Value Unit.
     */
    @Basic
    public float getRvu()
    {
        return fRvu;
    }

    /**
     * Sets the Relative Value Unit (RVU). For reflection-based construction only.
     */
    void setRvu(final float rvu)
    {
        this.fRvu = rvu;
    }
    
    /**
     * Returns the full procedure description.
     * @return a String no longer than {@link #DESCRIPTION_MAX} characters
     */
    @Basic
    @Column(nullable = false, length = DESCRIPTION_MAX)
    public String getLongDescription()
    {
        return fLongDescription;
    }

    /**
     * Sets the full procedure description. For reflection-based construction only.
     * @throws IllegalArgumentException if the given string is empty or greater than
     * {@value #DESCRIPTION_MAX} characters
     */
    void setLongDescription(final String description)
    {
        fLongDescription = Preconditions.requireWithin(description, 1, DESCRIPTION_MAX);
    }

    /**
     * Returns a shortened version of the procedure description. Some procedures do not
     * have a shortened description, so the returned String may be equal to {@link
     * #getLongDescription()}.
     * @return a String no longer than {@link #DESCRIPTION_MAX} characters
     */
    @Basic
    @Column(nullable = false, length = DESCRIPTION_MAX)
    public String getShortDescription()
    {
        return fShortDescription;
    }

    /**
     * Sets the shortened procedure description. For reflection-based construction only.
     * @throws IllegalArgumentException if the given string is empty or greater than
     * {@value #DESCRIPTION_MAX} characters
     */
    void setShortDescription(final String description)
    {
        fShortDescription = Preconditions.requireWithin(description, 1, DESCRIPTION_MAX);
    }
    
    /**
     * Returns a String describing the procedure's surgical complexity. (This may only
     * be relevant for the VA.)
     * @return a String no longer than {@link #COMPLEXITY_MAX} characters
     */
    @Basic
    @Column(nullable = false, length = COMPLEXITY_MAX)
    public String getComplexity()
    {
        return fComplexity;
    }
    
    /**
     * Sets the surgical complexity string. For reflection-based construction only.
     * @throws IllegalArgumentException if the given string is empty or greater than
     * {@value #COMPLEXITY_MAX} characters
     */
    void setComplexity(final String complexity)
    {
        fComplexity = Preconditions.requireWithin(complexity, 1, COMPLEXITY_MAX);
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
     * Sets whether this procedure is eligible for a risk calculation. For
     * reflection-based construction only.
     */
    void setEligible(final boolean eligible)
    {
        fEligible = eligible;
    }

    /**
     * Returns the same as {@link #getShortString()}.
     */
    @Override
    public String toString()
    {
        return getShortString();
    }
    
    /**
     * Returns a String in the format "cptCode - shortDesc - RVU".
     */
    @Transient
    public String getShortString()
    {
        return String.format("%s - %s (%s)",
                // Use Float.toString() to get only as many fractional part
                // digits as we need (e.g., 10.0 or 5.56).
                getCptCode(), getShortDescription(), Float.toString(getRvu()));
    }
    
    /**
     * Returns a String in the format "cptCode - longDesc - RVU".
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


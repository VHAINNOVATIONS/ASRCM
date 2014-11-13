package gov.va.med.srcalc.domain;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents a medical procedure, particuarly a CPT code.
 * @author david
 *
 */
@Entity
public class Procedure
{
    private int fId;
    
    private String fCptCode;
    
    private int fRvu;
    
    private String fDescription;

    /**
     * Package-private default constructor mainly for Hibernate use. Be very
     * careful about using this constructor as this class presents an immutable
     * public interface.
     */
    Procedure()
    {
    }
    
    public Procedure(final int id, final String cptCode, final int rvu, final String description)
    {
        this.fId = id;
        this.fCptCode = cptCode;
        this.fRvu = rvu;
        this.fDescription = description;
    }

    @Id // We use method-based property detection throughout the app.
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
    public int getRvu()
    {
        return fRvu;
    }

    /**
     * For reflection-based construction only.
     */
    public void setRvu(final int rvu)
    {
        this.fRvu = rvu;
    }

    @Basic
    public String getDescription()
    {
	return fDescription;
    }

    /**
     * For reflection-based construction only.
     */
    public void setDescription(final String description)
    {
	fDescription = description;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s - %s (%d)",
                getCptCode(), getDescription(), getRvu());
    }
    
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof Procedure)  // false if o == null
        {
            final Procedure other = (Procedure)o;
            
            // Compare "business equality", not using the primary key, per
            // Hibernate recommendation.
            return this.getCptCode().equals(other.getCptCode());
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getCptCode());
    }
}


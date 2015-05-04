package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.util.NoNullSet;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

/**
 * <p>Represents a surgical specialty, with the associated calcuation variables.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
public final class Specialty implements Serializable
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    private int fId;
    
    private int fVistaId;
    
    private String fName;
    
    private Set<RiskModel> fRiskModels = new HashSet<>();

    public Specialty()
    {
    }
    
    public Specialty(final int vistaId, final String name)
    {
        this.fVistaId = vistaId;
        this.fName = name;
    }

    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id // We use method-based property detection throughout the app.
    public int getId()
    {
        return fId;
    }

    /**
     * For reflection-based construction only. Business code should never modify
     * the surrogate key as it is generated from the database.
     */
    void setId(final int id)
    {
        this.fId = id;
    }

    /**
     * The specialty's national VistA specialty ID (SURGICAL SPECIALTY file). Note
     * that two SRCalc specialties (e.g., General and Other) may have the same
     * VistA ID.
     */
    @Basic
    public int getVistaId()
    {
        return fVistaId;
    }

    /**
     * For reflection-based construction only. The application assumes that the
     * VistA ID does not change and therefore uses this field for value equality.
     */
    void setVistaId(final int vistaId)
    {
        this.fVistaId = vistaId;
    }

    @Basic
    public String getName()
    {
	return fName;
    }

    public void setName(final String name)
    {
	fName = name;
    }
    
    /**
     * Returns all {@link Variable}s required by all associated {@link
     * RiskModel}s. Caution: lazy-loaded.
     * @return an unmodifiable set that does not contain null
     */
    @Transient
    public NoNullSet<Variable> getModelVariables()
    {
        final HashSet<Variable> allVariables = new HashSet<>();
        for (final RiskModel model : getRiskModels())
        {
            allVariables.addAll(model.getRequiredVariables());
        }
        return NoNullSet.fromSet(Collections.unmodifiableSet(allVariables));
    }
    
    /**
     * Returns all {@link RiskModel}s associated with the Specialty. Caution:
     * lazy-loaded.
     */
    @OneToMany(fetch = FetchType.LAZY)
    // Override strange defaults. See
    // <https://forum.hibernate.org/viewtopic.php?f=1&t=1037190>.
    @JoinTable(
            name = "specialty_risk_model",
            joinColumns = @JoinColumn(name = "specialty_id"),
            inverseJoinColumns = @JoinColumn(name = "risk_model_id")
        )
    public Set<RiskModel> getRiskModels()
    {
        return fRiskModels;
    }

    /**
     * For reflection-based construction only. The collection should be modified
     * via {@link #getRiskModels()}.
     */
    public void setRiskModels(Set<RiskModel> riskModels)
    {
        fRiskModels = riskModels;
    }

    @Override
    public String toString()
    {
        return fName;
    }
    
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof Specialty)  // false if o == null
        {
            final Specialty other = (Specialty)o;
            
            // Compare the VistA ID and name as this pair should always be
            // unique.
            return (this.getVistaId() == other.getVistaId()) &&
                   Objects.equals(this.getName(), (other.getName()));
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getVistaId(), getName());
    }
}


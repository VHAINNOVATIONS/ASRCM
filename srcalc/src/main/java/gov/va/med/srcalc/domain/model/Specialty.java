package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.util.Preconditions;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import com.google.common.collect.ImmutableSet;

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
     * The maximum length of a valid surgical specialty: {@value}. VistA
     * surgical specialties are 40 characters or less, but we support more
     * in case we want to add a suffix (e.g., two different cardiac models).
     */
    public static final int SPECIALTY_NAME_MAX = 100;
    
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
    
    /**
     * Constructs an instance.
     * @param vistaId See {@link #getVistaId()}
     * @param name See {@link #getName()}
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if any argument is illegal. (See setter
     * Javadocs.)
     */
    public Specialty(final int vistaId, final String name)
    {
        // Use the setters to enforce constraints.
        setVistaId(vistaId);
        setName(name);
    }

    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id // We use method-based property detection throughout the app.
    @GeneratedValue
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
    public final int getVistaId()
    {
        return fVistaId;
    }

    /**
     * For reflection-based construction only. The application assumes that the
     * VistA ID does not change and therefore uses this field for value equality.
     */
    final void setVistaId(final int vistaId)
    {
        this.fVistaId = vistaId;
    }

    /**
     * The display name of the specialty. Will be no longer than
     * {@link #SPECIALTY_NAME_MAX} characters.
     */
    @Basic
    @Column(
            nullable = false,
            length = SPECIALTY_NAME_MAX)
    public final String getName()
    {
	return fName;
    }

    /**
     * Sets the display name of the specialty.
     * @throws NullPointerException if the provided name is null
     * @throws IllegalArgumentException if the provided name is empty or longer
     * than {@link #SPECIALTY_NAME_MAX}.
     */
    public final void setName(final String name)
    {
	fName = Preconditions.requireWithin(name, 1, SPECIALTY_NAME_MAX);
    }
    
    /**
     * Returns all {@link Variable}s required by all associated {@link
     * RiskModel}s. Caution: lazy-loaded.
     * @return an ImmutableSet
     */
    @Transient
    public ImmutableSet<Variable> getModelVariables()
    {
        final HashSet<Variable> allVariables = new HashSet<>();
        for (final RiskModel model : getRiskModels())
        {
            allVariables.addAll(model.getRequiredVariables());
        }
        return ImmutableSet.copyOf(allVariables);
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


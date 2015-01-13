package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.Variable;

import java.util.*;

import javax.persistence.*;

/**
 * A risk model definition.
 */
@Entity
public class RiskModel
{
    public static final int DISPLAY_NAME_MAX = 80;
    
    private int fId;
    private String fDisplayName;
    private Set<ModelTerm> fTerms = new HashSet<>();
    
    /**
     * Mainly intended for reflection-based construction. Business code should
     * use {@link #RiskModel(String)}.
     */
    public RiskModel()
    {
    }
    
    public RiskModel(final String displayName)
    {
        fDisplayName = displayName;
    }
    
    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id
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

    @Basic(optional = false)
    @Column(length = DISPLAY_NAME_MAX)
    public String getDisplayName()
    {
        return fDisplayName;
    }

    /**
     * Sets the display name.
     * @param displayName must not be null
     * @throws NullPointerException if displayName is null
     * @throws IllegalArgumentException if displayName is longer than {@link #DISPLAY_NAME_MAX}
     */
    public void setDisplayName(final String displayName)
    {
        if (displayName.length() > DISPLAY_NAME_MAX)
        {
            throw new IllegalArgumentException(
                    "The display name must be 80 characters or less.");
        }
        fDisplayName = displayName;
    }

    /**
     * The various terms in the model's sum.
     */
    @OneToMany(fetch = FetchType.EAGER) // eager-load due to close association
    // Override strange defaults. See
    // <https://forum.hibernate.org/viewtopic.php?f=1&t=1037190>.
    @JoinTable(
            name = "risk_model_term",
            joinColumns = @JoinColumn(name = "risk_model_id"),
            inverseJoinColumns = @JoinColumn(name = "model_term_id"))
    public Set<ModelTerm> getTerms()
    {
        return fTerms;
    }

    /**
     * For reflection-based construction only. Business code should modify the
     * Set returned by {@link #getTerms()}.
     */
    void setTerms(final Set<ModelTerm> terms)
    {
        fTerms = terms;
    }
    
    /**
     * <p>Returns the set of all Variables required for the model.</p>
     * 
     * <p>Note that Variables define equality as identity, so two different
     * Variable instances with exactly the same attributes may be put into the
     * Set.</p>
     * @return an unmodifiable set
     */
    @Transient
    public Set<Variable> getRequiredVariables()
    {
        final HashSet<Variable> allVariables = new HashSet<>();
        for (final ModelTerm term : getTerms())
        {
            allVariables.addAll(term.getRequiredVariables());
        }
        return Collections.unmodifiableSet(allVariables);
    }
    
    // TODO: implement equals() and hashCode()
}

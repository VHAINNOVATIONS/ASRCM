package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.util.NoNullSet;

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
    private ConstantTerm fConstantTerm = new ConstantTerm(0.0f);
    private Set<BooleanTerm> fBooleanTerms = new HashSet<>();
    private Set<DiscreteTerm> fDiscreteTerms = new HashSet<>();
    private Set<NumericalTerm> fNumericalTerms = new HashSet<>();
    private Set<ProcedureTerm> fProcedureTerms = new HashSet<>();
    
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
     * The {@link ConstantTerm} for the model. Only one is supported because
     * multiple constants doesn't make sense.
     */
    @Embedded
    @AttributeOverrides({
        // Call the coefficient column the "constant" for clarity in the schema.
        @AttributeOverride(name="coefficient", column = @Column(name="constant"))
    })
    public ConstantTerm getConstantTerm()
    {
        return fConstantTerm;
    }

    void setConstantTerm(final ConstantTerm constantTerm)
    {
        fConstantTerm = Objects.requireNonNull(constantTerm);
    }

    /**
     * <p>The boolean terms in the model's sum. Mutable.</p>
     */
    @ElementCollection(fetch = FetchType.EAGER) // eager-load due to close association
    // Override strange defaults.
    @CollectionTable(
            name = "risk_model_boolean_term",
            joinColumns = @JoinColumn(name = "risk_model_id"))
    public Set<BooleanTerm> getBooleanTerms()
    {
        return fBooleanTerms;
    }

    /**
     * For reflection-based construction only. Business code should modify the
     * Set returned by {@link #getBooleanTerms()}.
     */
    void setBooleanTerms(final Set<BooleanTerm> terms)
    {
        fBooleanTerms = terms;
    }

    /**
     * <p>The {@link DiscreteTerm}s in the model's sum. Mutable.</p>
     * 
     * <p>See {@link #getTerms()} for a read-only view of all of the terms.</p>
     */
    @ElementCollection(fetch = FetchType.EAGER) // eager-load due to close association
    // Override strange defaults.
    @CollectionTable(
            name = "risk_model_discrete_term",
            joinColumns = @JoinColumn(name = "risk_model_id"))
    public Set<DiscreteTerm> getDiscreteTerms()
    {
        return fDiscreteTerms;
    }

    /**
     * For reflection-based construction only. Business code should modify the
     * Set returned by {@link #getDiscreteTerms()}.
     */
    void setDiscreteTerms(Set<DiscreteTerm> disceteTerms)
    {
        fDiscreteTerms = disceteTerms;
    }

    /**
     * <p>The {@link NumericalTerm}s in the model's sum. Mutable.</p>
     * 
     * <p>See {@link #getTerms()} for a read-only view of all of the terms.</p>
     */
    @ElementCollection(fetch = FetchType.EAGER) // eager-load due to close association
    // Override strange defaults.
    @CollectionTable(
            name = "risk_model_numerical_term",
            joinColumns = @JoinColumn(name = "risk_model_id"))
    public Set<NumericalTerm> getNumericalTerms()
    {
        return fNumericalTerms;
    }

    /**
     * For reflection-based construction only. Business code should modify the
     * Set returned by {@link #getNumericalTerms()}.
     */
    void setNumericalTerms(Set<NumericalTerm> numericalTerms)
    {
        fNumericalTerms = numericalTerms;
    }

    /**
     * <p>The {@link DerivedTerm}s in the model's sum. Mutable.</p>
     * 
     * <p>See {@link #getTerms()} for a read-only view of all of the terms.</p>
     */
    @ElementCollection(fetch = FetchType.EAGER) // eager-load due to close association
    // Override strange defaults.
    @CollectionTable(
            name = "risk_model_procedure_term",
            joinColumns = @JoinColumn(name = "risk_model_id"))
    public Set<ProcedureTerm> getProcedureTerms()
    {
        return fProcedureTerms;
    }

    /**
     * For reflection-based construction only. Business code should modify the
     * Set returned by {@link #getProcedureTerms()}.
     */
    void setProcedureTerms(Set<ProcedureTerm> derivedTerms)
    {
        fProcedureTerms = derivedTerms;
    }
    
    /**
     * <p>Returns an unmodifiable set of all of the {@link ModelTerm}s.</p>
     * 
     * <p>If you want to modify the set of terms, you must use one of the above
     * type-specific accessors like {@link #getBooleanTerms()}. The different
     * types of ModelTerms are provided via different accessors due to
     * Hibernate's lack of support for polymorphic ElementCollections
     * (HHH-1910).</p>
     */
    @Transient
    public Set<ModelTerm> getTerms()
    {
        final NoNullSet<ModelTerm> terms = NoNullSet.fromSet(new HashSet<ModelTerm>());
        terms.add(getConstantTerm());
        terms.addAll(getBooleanTerms());
        terms.addAll(getDiscreteTerms());
        terms.addAll(getNumericalTerms());
        terms.addAll(getProcedureTerms());
        return Collections.unmodifiableSet(terms);
    }

    /**
     * <p>Returns the set of all Variables required for the model.</p>
     * 
     * <p>Note that Variables define equality as identity, so two different
     * Variable instances with exactly the same attributes may be put into the
     * Set.</p>
     * @return an unmodifiable set that does not contain null
     */
    @Transient
    public NoNullSet<Variable> getRequiredVariables()
    {
        final HashSet<Variable> allVariables = new HashSet<>();
        for (final ModelTerm term : getTerms())
        {
            allVariables.addAll(term.getRequiredVariables());
        }
        return NoNullSet.fromSet(Collections.unmodifiableSet(allVariables));
    }
    
    // TODO: implement equals() and hashCode()
}

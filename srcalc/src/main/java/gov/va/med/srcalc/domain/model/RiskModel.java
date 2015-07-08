package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.Value;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.MissingValuesException;
import gov.va.med.srcalc.util.Preconditions;

import java.util.*;
import java.util.regex.Pattern;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * A risk model definition.
 */
@Entity
public class RiskModel implements Comparable<RiskModel>
{
    private static final Logger fLogger = LoggerFactory.getLogger(RiskModel.class);
    
    private int fId;
    private String fDisplayName;
    private ConstantTerm fConstantTerm = new ConstantTerm(0.0f);
    private Set<BooleanTerm> fBooleanTerms = new HashSet<>();
    private Set<DiscreteTerm> fDiscreteTerms = new HashSet<>();
    private Set<NumericalTerm> fNumericalTerms = new HashSet<>();
    private Set<ProcedureTerm> fProcedureTerms = new HashSet<>();
    private Set<DerivedTerm> fDerivedTerms = new HashSet<>();
    
    // Use the same Char set and Reg Expr as for the Variable names.
    /**
     * English description of the valid display name characters for readable
     * error messages.
     * @see #VALID_DISPLAY_NAME_REGEX
     */
    public static final String VALID_MODEL_NAME_CHARACTERS = DisplayNameConditions.VALID_DISPLAY_NAME_CHARACTERS;
    
    public static final String VALID_MODEL_NAME_REGEX = DisplayNameConditions.VALID_DISPLAY_NAME_REGEX;

    /**
     * Precompiled version of {@link RiskModel#VALID_DISPLAY_NAME_REGEX} for
     * efficiency.
     */
    public static final Pattern VALID_MODEL_NAME_PATTERN =
            Pattern.compile(RiskModel.VALID_MODEL_NAME_REGEX);

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

    @Basic(optional = false)
    @Column(length = DisplayNameConditions.DISPLAY_NAME_MAX)
    public String getDisplayName()
    {
        return fDisplayName;
    }

    /**
     * Sets the display name.
     * @param displayName must not be null
     * @throws NullPointerException if displayName is null
     * @throws IllegalArgumentException if displayName is empty or longer than
     * {@link DisplayNameConditions#DISPLAY_NAME_MAX}
     */
    public void setDisplayName(final String displayName)
    {
        fDisplayName = Preconditions.requireWithin(
                displayName, 1, DisplayNameConditions.DISPLAY_NAME_MAX);
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
     * <p>The {@link ProcedureTerm}s in the model's sum. Mutable.</p>
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
    void setProcedureTerms(Set<ProcedureTerm> procedureTerms)
    {
        fProcedureTerms = procedureTerms;
    }
    
    /**
     * <p>The {@link DerivedTerm}s in the model's sum. Mutable.</p>
     * 
     * <p>See {@link #getTerms()} for a read-only view of all of the terms.</p>
     */
    @ElementCollection(fetch = FetchType.EAGER) // eager-load due to close association
    // Override strange defaults.
    @CollectionTable(
            name = "risk_model_derived_term",
            joinColumns = @JoinColumn(name = "risk_model_id"))
    public Set<DerivedTerm> getDerivedTerms()
    {
        return fDerivedTerms;
    }

    /**
     * For reflection-based construction only. Business code should modify the
     * Set returned by {@link #getProcedureTerms()}.
     */
    void setDerivedTerms(Set<DerivedTerm> derivedTerms)
    {
        fDerivedTerms = derivedTerms;
    }
    
    /**
     * <p>Returns an unmodifiable set of all of the {@link ModelTerm}s.</p>
     * 
     * <p>If you want to modify the set of terms, you must use one of the above
     * type-specific accessors like {@link #getBooleanTerms()}. The different
     * types of ModelTerms are provided via different accessors due to
     * Hibernate's lack of support for polymorphic ElementCollections
     * (HHH-1910).</p>
     * @return an ImmutableSet
     */
    @Transient
    public ImmutableSet<ModelTerm> getTerms()
    {
        // Use a builder to assemble all the subsets into one ImmutableSet
        // without an intermediate set.
        return ImmutableSet.<ModelTerm>builder()
                .add(getConstantTerm())
                .addAll(getBooleanTerms())
                .addAll(getDiscreteTerms())
                .addAll(getNumericalTerms())
                .addAll(getProcedureTerms())
                .addAll(getDerivedTerms())
                .build();
    }

    /**
     * <p>Returns the set of all Variables required for the model.</p>
     * 
     * <p>Note that Variables define equality as identity, so two different
     * Variable instances with exactly the same attributes may be put into the
     * Set.</p>
     * @return an ImmutableSet
     */
    @Transient
    public ImmutableSet<Variable> getRequiredVariables()
    {
        final HashSet<Variable> allVariables = new HashSet<>();
        for (final ModelTerm term : getTerms())
        {
            allVariables.addAll(term.getRequiredVariables());
        }
        return ImmutableSet.copyOf(allVariables);
    }
    
    @Override
    public String toString()
    {
        return String.format(
                "RiskModel \"%s\" with ID=%d, and %d terms",
                getDisplayName(),
                getId(),
                // -1 to subtract out the constant term
                getTerms().size() - 1);
    }
    
    /**
     * Calculates the result of this model.
     * @param inputValues the input values. There must be exactly one input value
     * per required variable.
     * @return the calculated result
     * @throws IllegalArgumentException if there is not exactly one input value
     * per required variable.
     * @throws MissingValuesException if there are any required variables without an assigned value
     */
    public double calculate(final Collection<Value> inputValues) throws MissingValuesException
    {
        fLogger.debug("Calculating {}", this);
        
        // Build a Map(Variable -> Value)
        final HashMap<Variable, Value> valueMap = new HashMap<>(inputValues.size());
        for (final Value v : inputValues)
        {
            final Value previousEntry = valueMap.put(v.getVariable(), v);
            // Check precondition: only one value per variable. (The at least
            // one value part is checked with each term below.)
            if (previousEntry != null)
            {
                throw new IllegalArgumentException(
                        "Multiple values provided for Variable " + v.getVariable());
            }
        }
        
        double sum = 0.0;
        // TODO: Change to a Set rather than List.
        final List<MissingValueException> missingList = new ArrayList<MissingValueException>();
        for (final ModelTerm term : getTerms())
        {
            try
            {
                final double termSummand = term.getSummand(valueMap);
                fLogger.debug("Adding {} for {}", termSummand, term);
                sum += termSummand;
            }
            catch(final MissingValuesException e)
            {
                missingList.addAll(e.getMissingValues());
            }
        }
        
        if(missingList.size() > 0)
        {
            throw new MissingValuesException("The calculation is missing values.", missingList);
        }
        final double expSum = Math.exp(sum);
        
        return expSum / (1 + expSum);
    }
    
    /**
     * Returns a true if a RiskModel and if displayname and all Terms are equal.
     */
    @Override
    public final boolean equals(final Object o)
    {
        if( this == o )
        {
            return true;
        }
        else if( o == null || !(o instanceof RiskModel) ) 
        {
            return false;
        }
        else 
        {
            // don't include the Id (Hibernate best practices)
            final RiskModel other = (RiskModel)o;
            
            if( this.getDisplayName().compareTo( other.getDisplayName() ) != 0 ) 
            {
                return false;
            }        
            
            return Objects.equals( getTerms(), other.getTerms() );
        }
    }

    /**
     * Returns a hash code based on the displayName and the model terms.
     */
    @Override
    public final int hashCode()
    {
        return Objects.hash( getDisplayName(), getTerms() );
    }

    /**
     * Compares RiskModels based on their display name. Not consistent with
     * equals!
     */
    @Override
    public int compareTo(final RiskModel other)
    {
        return this.fDisplayName.compareTo(other.fDisplayName);
    }
    
}

package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.model.Procedure;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.Preconditions;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.Immutable;
import org.joda.time.DateTime;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

/**
 * <p>Represents a signed, immutable calculation result.</p>
 * 
 * <p>This class is similar to {@link CalculationResult} but stores only primitive
 * values and is suitable for storing in a database.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
@Immutable
public final class SignedResult
{
    private int fId;
    private HistoricalCalculation fHistoricalCalculation;
    private int fPatientDfn;
    private Optional<String> fCptCode;
    private Date fSignatureTimestamp;
    private ImmutableMap<String, String> fInputs;
    private ImmutableMap<String, Float> fOutcomes;
    
    /**
     * Intended for reflection-based construction only. Business code should use the other
     * constructor.
     */
    SignedResult()
    {
    }
    
    /**
     * Constructs an instance with the given properties.
     * @param historicalCalc See {@link #getHistoricalCalculation()}.
     * @param patientDfn See {@link #getPatientDfn()}.
     * @param cptCode See {@link #getCptCode()}.
     * @param signatureTimestamp See {@link #getSignatureTimestamp()}.
     * @param inputs See {@link #getInputs()}.
     * @param outcomes See {@link #getOutcomes()}.
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if the CPT code is present and not {@link
     * Procedure#CPT_CODE_LENGTH} characters
     */
    public SignedResult(
            final HistoricalCalculation historicalCalc,
            final int patientDfn,
            final Optional<String> cptCode,
            final DateTime signatureTimestamp,
            final Map<String, String> inputs,
            final Map<String, Float> outcomes)
    {
        // Use setters to verify constraints.
        setHistoricalCalculation(historicalCalc);
        setPatientDfn(patientDfn);
        setCptCode(cptCode);
        // See getSignatureTimestamp() for why we enforce 0 millis of second.
        setSignatureTimestamp(signatureTimestamp.withMillisOfSecond(0).toDate());
        setInputs(ImmutableMap.copyOf(inputs));
        setOutcomes(ImmutableMap.copyOf(outcomes));
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
        fId = id;
    }
    
    /**
     * Returns the associated historical calculation information.
     * @return never null
     */
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "run_id")
    @MapsId
    public HistoricalCalculation getHistoricalCalculation()
    {
        return fHistoricalCalculation;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setHistoricalCalculation(final HistoricalCalculation historicalCalc)
    {
        fHistoricalCalculation = Objects.requireNonNull(historicalCalc);
    }

    /**
     * Returns the DFN of the associated patient.
     */
    @Basic
    public int getPatientDfn()
    {
        return fPatientDfn;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setPatientDfn(final int patientDfn)
    {
        fPatientDfn = patientDfn;
    }
    
    /**
     * Returns the CPT code of the associated procedure, if there was one.
     * @return an Optional containing the CPT code, if applicable
     */
    @Transient  // Hibernate uses getCptCodeNullabe() instead
    public Optional<String> getCptCode()
    {
        return fCptCode;
    }
    
    /**
     * Sets the cptCode, checking preconditions.
     * @throws IllegalArgumentException if the cptCode is not {@link
     * Procedure#CPT_CODE_LENGTH} characters
     */
    private void setCptCode(final Optional<String> optional)
    {
        if (optional.isPresent())
        {
            Preconditions.requireWithin(
                    optional.get(), Procedure.CPT_CODE_LENGTH, Procedure.CPT_CODE_LENGTH);
        }

        fCptCode = optional;
    }
    
    /**
     * As {@link #getCptCode()}, but represents a missing CPT Code as null. Purely to
     * support Hibernate, which does not support Guava's Optional class.
     * @return the optional CPT Code as a nullable string
     */
    @Column(name = "cpt_code", nullable = true, length = Procedure.CPT_CODE_LENGTH)
    String getCptCodeNullable()
    {
        return fCptCode.orNull();
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     * @throws IllegalArgumentException if the cptCode is not {@link
     * Procedure#CPT_CODE_LENGTH} characters
     */
    void setCptCodeNullable(final String cptCode)
    {
        setCptCode(Optional.fromNullable(cptCode));
    }
    
    /**
     * Returns when the user signed the calculation. We do not track milliseconds, so this
     * Date object will always represent a particular second on-the-dot.
     */
    @Basic
    @Column(nullable = false)
    public Date getSignatureTimestamp()
    {
        return fSignatureTimestamp;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setSignatureTimestamp(final Date signatureTimestamp)
    {
        fSignatureTimestamp = Objects.requireNonNull(signatureTimestamp);
    }
    
    /**
     * Returns the calculation input values as a Map from variable key to value.
     * @return an immutable map
     */
    @ElementCollection(fetch = FetchType.EAGER)
    // Override various defaults for a better schema.
    @CollectionTable(
            name = "signed_result_input",
            joinColumns = @JoinColumn(name = "result_id"))
    @MapKeyColumn(name = "variable_key", nullable = false, length = Variable.KEY_MAX)
    // There is no set maximum length for the value string, but the default of 255 should
    // be sufficient.
    @Column(name = "variable_value", nullable = false)
    public Map<String, String> getInputs()
    {
        return fInputs;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setInputs(final Map<String, String> inputs)
    {
        // Note: normally with Hibernate we would preserve the passed collection since it
        // will be Hibernate's special implementation of Map. Since this object is
        // immutable, however, we don't care to keep Hibernate's mutable implementation
        // and just discard it.
        fInputs = ImmutableMap.copyOf(inputs);
    }
    
    /**
     * Returns the risk outcomes as a Map from risk model name to calculated
     * risk.
     * @return an immutable map
     */
    @ElementCollection(fetch = FetchType.EAGER)
    // Override various defaults for a better schema.
    @CollectionTable(
            name = "signed_result_outcome",
            joinColumns = @JoinColumn(name = "result_id"))
    @MapKeyColumn(
            name = "model_name",
            nullable = false,
            length = DisplayNameConditions.DISPLAY_NAME_MAX)
    @Column(name = "risk_result", nullable = false)
    public Map<String, Float> getOutcomes()
    {
        return fOutcomes;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setOutcomes(final Map<String, Float> outcomes)
    {
        // See note on setValues() above.
        fOutcomes = ImmutableMap.copyOf(outcomes);
    }
    
    /**
     * <p>Returns a string representation of this object.</p>
     * 
     * <p>The exact format is unspecified, but the string will contain the patientDfn,
     * cptCode, and {@link HistoricalCalculation#toString()}.</p>
     */
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("historicalCalc", fHistoricalCalculation)
                .add("patientDfn", fPatientDfn)
                .add("cptCode", fCptCode)
                .add("outcomes", fOutcomes)
                .add("signed", fSignatureTimestamp)
                .toString();
    }
    
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof SignedResult)
        {
            final SignedResult other = (SignedResult)o;
            
            return Objects.equals(this.fHistoricalCalculation, other.fHistoricalCalculation) &&
                    (this.fPatientDfn == other.fPatientDfn) &&
                    Objects.equals(this.fCptCode, other.fCptCode) &&
                    Objects.equals(this.fSignatureTimestamp, other.fSignatureTimestamp) &&
                    Objects.equals(this.fInputs, other.fInputs) &&
                    Objects.equals(this.fOutcomes, other.fOutcomes);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(
                fHistoricalCalculation,
                fPatientDfn,
                fCptCode,
                fSignatureTimestamp,
                fInputs,
                fOutcomes);
    }
}

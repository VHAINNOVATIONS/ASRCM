package gov.va.med.srcalc.domain.calculation;

import java.util.Map;
import java.util.Objects;

import org.joda.time.DateTime;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

/**
 * <p>Represents a signed, totally-immutable calculation result.</p>
 * 
 * <p>This class is similar to {@link CalculationResult} but stores only primitive
 * values and is suitable for storing in a database.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class SignedResult
{
    private final int fPatientDfn;
    private final String fSpecialtyName;
    private final Optional<String> fCptCode;
    private final DateTime fStartDateTime;
    private final DateTime fSignatureDateTime;
    private final ImmutableMap<String, Float> fOutcomes;
    
    public SignedResult(
            final int patientDfn,
            final String specialtyName,
            final Optional<String> cptCode,
            final DateTime startDateTime,
            final DateTime signatureDateTime,
            final Map<String, Float> outcomes)
    {
        fPatientDfn = patientDfn;
        fSpecialtyName = specialtyName;
        fCptCode = cptCode;
        // See getStartDateTime().
        fStartDateTime = startDateTime.withMillisOfSecond(0);
        // See getSignatureDateTime().
        fSignatureDateTime = signatureDateTime.withMillisOfSecond(0);
        fOutcomes = ImmutableMap.copyOf(outcomes);
    }
    
    /**
     * Returns the DFN of the associated patient.
     */
    public int getPatientDfn()
    {
        return fPatientDfn;
    }

    /**
     * Returns the name of the associated surgical specialty.
     */
    public String getSpecialtyName()
    {
        return fSpecialtyName;
    }
    
    /**
     * Returns the CPT code of the associated procedure, if there was one.
     * @return an Optional containing the CPT code, if applicable
     */
    public Optional<String> getCptCode()
    {
        return fCptCode;
    }
    
    /**
     * The DateTime at which the user started the calculation. We do not track
     * milliseconds, so the millisOfSecond field will always be 0 to facilitate
     * comparisons.
     */
    public DateTime getStartDateTime()
    {
        return fStartDateTime;
    }
    
    /**
     * The DateTime at which the user signed the calculation. We do not track
     * milliseconds, so the millisOfSecond field will always be 0 to facilitate
     * comparisons.
     */
    public DateTime getSignatureDateTime()
    {
        return fSignatureDateTime;
    }
    
    /**
     * Returns the risk outcomes as a Map from risk model name to calculated
     * risk.
     */
    public ImmutableMap<String, Float> getOutcomes()
    {
        return fOutcomes;
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("patientDfn", fPatientDfn)
                .add("specialty", fSpecialtyName)
                .add("cptCode", fCptCode)
                .add("start", fStartDateTime)
                .add("signed", fSignatureDateTime)
                .toString();
    }
    
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof SignedResult)
        {
            final SignedResult other = (SignedResult)o;
            
            return (this.fPatientDfn == other.fPatientDfn) &&
                    Objects.equals(this.fSpecialtyName, other.fSpecialtyName) &&
                    Objects.equals(this.fCptCode, other.fCptCode) &&
                    Objects.equals(this.fStartDateTime, other.fStartDateTime) &&
                    Objects.equals(this.fSignatureDateTime, other.fSignatureDateTime) &&
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
                fPatientDfn,
                fSpecialtyName,
                fCptCode,
                fStartDateTime,
                fSignatureDateTime,
                fOutcomes);
    }
}

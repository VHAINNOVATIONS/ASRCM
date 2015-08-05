package gov.va.med.srcalc.domain.calculation;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

/**
 * <p>Encapsulates all information about a historical calculation run. Similar to a Data
 * Transfer Object, but not intended to carry data between processes.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class HistoricalRunInfo
{
    private final HistoricalCalculation fHistoricalCalculation;
    private final Optional<SignedResult> fSignedResult;
    
    /**
     * Constructs an instance with the given properties.
     * @param historicalCalc See {@link #getHistoricalCalculation()}.
     * @param signedResult See {@link #getSignedResult()}.
     * @throws NullPointerException if either argument is null
     */
    public HistoricalRunInfo(
            final HistoricalCalculation historicalCalc,
            final Optional<SignedResult> signedResult)
    {
        fHistoricalCalculation = Objects.requireNonNull(historicalCalc);
        fSignedResult = Objects.requireNonNull(signedResult);
    }
    
    /**
     * Constructs an instance from an unsigned HistoricalCalculation.
     * @param calc not null
     * @return a new instance with an absent SignedResult
     * @throws NullPointerException if the argument is null
     */
    public static HistoricalRunInfo unsigned(final HistoricalCalculation calc)
    {
        return new HistoricalRunInfo(calc, Optional.<SignedResult>absent());
    }
    
    /**
     * Constructs an instance from a SignedResult.
     * @param signedResult not null
     * @throws NullPointerException if the argument is null
     */
    public static HistoricalRunInfo signed(final SignedResult signedResult)
    {
        return new HistoricalRunInfo(
                signedResult.getHistoricalCalculation(), Optional.of(signedResult));
    }
    
    /**
     * Returns the {@link HistoricalCalculation} object encapsulating calculation
     * metadata.
     * @return never null
     */
    public HistoricalCalculation getHistoricalCalculation()
    {
        return fHistoricalCalculation;
    }
    
    /**
     * Returns the {@link SignedResult} object encapsulating the signature data, if the
     * result was signed.
     * @return never null
     */
    public Optional<SignedResult> getSignedResult()
    {
        return fSignedResult;
    }
    
    /**
     * Returns the {@link SignedResult} object if the result was signed, or null
     * otherwise. Typically calling code should use {@link #getSignedResult()} but some
     * libraries (particularly Expression Language) have trouble with the Optional class.
     * @return the object or null
     */
    public SignedResult getSignedResultNullable()
    {
        return fSignedResult.orNull();
    }
    
    /**
     * Returns true if the user signed the calculation, false otherwise. This is a
     * shortcut for {@code getSignedResult().isPresent()}.
     */
    public boolean isSigned()
    {
        return fSignedResult.isPresent();
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("historicalCalc", fHistoricalCalculation)
                .add("signedResult", fSignedResult)
                .toString();
    }
    
    @Override
    public boolean equals(final Object obj)
    {
        // Performance optimization.
        if (obj == this)
        {
            return true;
        }
        
        if (obj instanceof HistoricalRunInfo)
        {
            final HistoricalRunInfo other = (HistoricalRunInfo)obj;
            
            return
                Objects.equals(this.fHistoricalCalculation, other.fHistoricalCalculation) &&
                Objects.equals(this.fSignedResult, other.fSignedResult);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(fHistoricalCalculation, fSignedResult);
    }
}

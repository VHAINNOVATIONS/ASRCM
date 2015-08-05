package gov.va.med.srcalc.web.view.admin;

import java.util.Collection;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import gov.va.med.srcalc.domain.calculation.HistoricalCalculation;
import gov.va.med.srcalc.domain.calculation.HistoricalRunInfo;
import gov.va.med.srcalc.domain.calculation.SignedResult;

/**
 * <p>Encapsulates summary statistics for a Utilization Report.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class UtilizationSummary
{
    private final int fTotalCount;
    private final int fSignedCount;
    private final int fSecondsToFirstRunAverage;
    private final int fSecondsToSignAverage;
    
    /**
     * Constructs an instance with the given properties.
     * @param totalCount See {@link #getTotalCount()}.
     * @param signedCount See {@link #getSignedCount()}.
     * @param secondsToFirstRunAverage See {@link #getSecondsToFirstRunAverage()}.
     * @param secondsToSignAverage See {@link #getSecondsToSignAverage()}.
     */
    public UtilizationSummary(
            final int totalCount,
            final int signedCount,
            final int secondsToFirstRunAverage,
            final int secondsToSignAverage)
    {
        fTotalCount = totalCount;
        fSignedCount = signedCount;
        fSecondsToFirstRunAverage = secondsToFirstRunAverage;
        fSecondsToSignAverage = secondsToSignAverage;
    }
    
    /**
     * Constructs an instance, calculating statistics from the given HistoricalRunInfo
     * objects.
     * @return a new instance encapsulating the statistics
     */
    public static UtilizationSummary fromRunInfos(
            final Collection<HistoricalRunInfo> runInfos)
    {
        // Calculate statistics via iteration. (A database is better at this, but it's
        // simpler just to code this than try to get the database to do it.)
        long secondsToFirstRunSum = 0;
        int signedCount = 0;
        long secondsToSignSum = 0;
        for (final HistoricalRunInfo runInfo: runInfos)
        {
            secondsToFirstRunSum += runInfo.getHistoricalCalculation().getSecondsToFirstRun();
            if (runInfo.isSigned())
            {
                ++signedCount;
                secondsToSignSum += runInfo.getSignedResult().get().getSecondsToSign();
            }
        }

        return new UtilizationSummary(
                runInfos.size(),
                signedCount,
                // We use longs to hold the sum, but the average should fit inside an int
                // (because all the components are ints).
                (int)(secondsToFirstRunSum / runInfos.size()),
                (int)(secondsToSignSum / signedCount));
    }
    
    /**
     * Returns the total number of calculations (including unsigned).
     */
    public int getTotalCount()
    {
        return fTotalCount;
    }
    
    /**
     * Returns the number of signed calculations.
     */
    public int getSignedCount()
    {
        return fSignedCount;
    }
    
    /**
     * Returns the mean of {@link HistoricalCalculation#getSecondsToFirstRun()}.
     */
    public int getSecondsToFirstRunAverage()
    {
        return fSecondsToFirstRunAverage;
    }
    
    /**
     * Returns the mean of {@link SignedResult#getSecondsToSignature()}.
     */
    public int getSecondsToSignAverage()
    {
        return fSecondsToSignAverage;
    }
    
    /**
     * Returns a string representing this object. The exact format is unspecified, but it
     * will contain the statistical properties.
     */
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("totalCount", fTotalCount)
                .add("signedCount", fSignedCount)
                .add("secondsToFirstRunAverage", fSecondsToFirstRunAverage)
                .add("secondsToSignAverage", fSecondsToSignAverage)
                .toString();
    }
    
    /**
     * Returns true if the given object is also a {@link UtilizationSummary} with the
     * same properties, false otherwise.
     */
    @Override
    public boolean equals(final Object obj)
    {
        // Performance optimization.
        if (obj == this)
        {
            return true;
        }
        
        if (obj instanceof UtilizationSummary)
        {
            final UtilizationSummary other = (UtilizationSummary)obj;
            
            return this.fTotalCount == other.fTotalCount &&
                    this.fSignedCount == other.fSignedCount &&
                    this.fSecondsToFirstRunAverage == other.fSecondsToFirstRunAverage &&
                    this.fSecondsToSignAverage == other.fSecondsToSignAverage;
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
                fTotalCount, fSignedCount, fSecondsToFirstRunAverage, fSecondsToSignAverage);
    }
}

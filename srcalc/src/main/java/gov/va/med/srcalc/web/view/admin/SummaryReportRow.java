package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
import java.util.Objects;

import gov.va.med.srcalc.domain.calculation.HistoricalCalculation;
import gov.va.med.srcalc.domain.calculation.SignedResult;

import org.joda.time.DateTime;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;

/**
 * Encapsulates a single row of data for a Summary Report.
 */
public final class SummaryReportRow
{
    /**
     * Since {@link SignedResult} is immutable, just store a reference to it instead of
     * copying the properties.
     */
    private final SignedResult fSignedResult;

    /**
     * The name of the risk model for this particular row. This value must be a key in
     * SignedResult.outcomes.
     */
    private final String fRiskModelName;
    
    public SummaryReportRow(final SignedResult signedResult, final String riskModelName)
    {
        fSignedResult = signedResult;
        fRiskModelName = riskModelName;
        
        // Fail-fast on a bad model name.
        if (!fSignedResult.getOutcomes().containsKey(fRiskModelName))
        {
            throw new IllegalArgumentException(
                    "Given riskModelName must be present in SignedResult.");
        }
    }
    
    /**
     * Makes a {@link SummaryReportRow} for each outcome in the given SignedResult.
     * @return a list in alphabetical order by risk model name
     */
    public static ImmutableList<SummaryReportRow> fromSignedResult(
            final SignedResult result)
    {
        final ArrayList<SummaryReportRow> rows = new ArrayList<>();
        for (final String modelName : ImmutableSortedSet.copyOf(
                String.CASE_INSENSITIVE_ORDER, result.getOutcomes().keySet()))
        {
            rows.add(new SummaryReportRow(result, modelName));
        }
        return ImmutableList.copyOf(rows);
    }
    
    /**
     * Returns the result's associated CPT code. Will be an empty string if the result
     * had no associated CPT code.
     * @return never null
     */
    public String getCptCode()
    {
        return fSignedResult.getCptCode().or("");
    }
    
    public String getSpecialtyName()
    {
        return fSignedResult.getHistoricalCalculation().getSpecialtyName();
    }
    
    public String getUserStation()
    {
        return fSignedResult.getHistoricalCalculation().getUserStation();
    }
    
    /**
     * Returns the Provider Type of the user who ran the calculation. An absent Provider
     * Type is represented by an empty string.
     * @see HistoricalCalculation#getProviderType()
     */
    public String getProviderType()
    {
        return fSignedResult.getHistoricalCalculation().getProviderType().or("");
    }
    
    public DateTime getSignatureTimestamp()
    {
        return fSignedResult.getSignatureTimestamp();
    }
    
    public String getRiskModelName()
    {
        return fRiskModelName;
    }
    
    public float getOutcome()
    {
        return fSignedResult.getOutcomes().get(fRiskModelName);
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("signedResult", fSignedResult)
                .add("riskModel", fRiskModelName)
                .toString();
    }
    
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof SummaryReportRow)
        {
            final SummaryReportRow other = (SummaryReportRow)obj;
            
            return Objects.equals(this.fSignedResult, other.fSignedResult) &&
                    Objects.equals(this.fRiskModelName, other.fRiskModelName);
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(fSignedResult, fRiskModelName);
    }
}

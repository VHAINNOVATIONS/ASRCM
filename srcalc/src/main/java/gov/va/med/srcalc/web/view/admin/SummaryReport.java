package gov.va.med.srcalc.web.view.admin;

import java.util.Objects;

import org.joda.time.LocalDate;

import com.google.common.base.MoreObjects;

import gov.va.med.srcalc.db.ResultSearchParameters;
import gov.va.med.srcalc.util.SearchResults;

/**
 * <p>Encapsulates Summary Report data.</p>
 * 
 * <p>The references in this class cannot be changed, but it is not truly immutable
 * because {@link ResultSearchParameters} is mutable.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class SummaryReport
{
    private final LocalDate fGenerationDate;
    private final ResultSearchParameters fParameters;
    private final SearchResults<SummaryReportRow> fResults;
    
    /**
     * Constructs an instance with the given properties and a generationDate of now.
     * @param parameters See {@link #getParameters()}.
     * @param results See {@link #getResults()}.
     * @throws NullPointerException if any argument is null
     */
    public SummaryReport(
            final ResultSearchParameters parameters,
            final SearchResults<SummaryReportRow> results)
    {
        fGenerationDate = LocalDate.now();
        fParameters = Objects.requireNonNull(parameters);
        fResults = Objects.requireNonNull(results);
    }
    
    /**
     * Returns the date this report was generated.
     */
    public LocalDate getGenerationDate()
    {
        return fGenerationDate;
    }
    
    /**
     * Returns the search parameters used to generate this report. The returned object
     * is mutable, but please don't modify it.
     */
    public ResultSearchParameters getParameters()
    {
        return fParameters;
    }
    
    /**
     * Returns the actual search results.
     */
    public SearchResults<SummaryReportRow> getResults()
    {
        return fResults;
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("generationDate", fGenerationDate)
                .add("parameters", fParameters)
                .add("results", fResults)
                .toString();
    }
    
    /**
     * <p>Returns true if the given object is also a SummaryReport with the same
     * properties.</p>
     * 
     * <p>Note that this operation relies on {@link ResultSearchParameters}, which is
     * mutable: take care using these objects in a Set or Map.</p>
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof SummaryReport)
        {
            final SummaryReport other = (SummaryReport)obj;

            return Objects.equals(this.fGenerationDate, other.fGenerationDate) &&
                    Objects.equals(this.fParameters, other.fParameters) &&
                    Objects.equals(this.fResults, other.fResults);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(fGenerationDate, fParameters, fResults);
    }
    
}

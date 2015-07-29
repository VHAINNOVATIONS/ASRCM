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
 */
public class SummaryReport
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
    
    public LocalDate getGenerationDate()
    {
        return fGenerationDate;
    }
    
    public ResultSearchParameters getParameters()
    {
        return fParameters;
    }
    
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
    
}

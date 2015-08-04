package gov.va.med.srcalc.web.view.admin;

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
    extends BaseReport<ResultSearchParameters, SummaryReportRow>
{
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
        super(parameters, results);
    }
    
}

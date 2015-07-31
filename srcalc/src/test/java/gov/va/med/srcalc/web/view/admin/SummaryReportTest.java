package gov.va.med.srcalc.web.view.admin;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import gov.va.med.srcalc.db.ResultSearchParameters;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.util.SearchResults;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * Unit tests for {@link SummaryReport}.
 */
public class SummaryReportTest
{
    @Test
    public final void testEqualsObject()
    {
        EqualsVerifier.forClass(SummaryReport.class).verify();
    }
    
    @Test
    public final void testBasic()
    {
        final ResultSearchParameters params = new ResultSearchParameters();
        final SearchResults<SummaryReportRow> results = new SearchResults<>(
                SummaryReportRow.fromSignedResult(SampleCalculations.signedThoracic()),
                true);
        final SummaryReport report = new SummaryReport(
                params, results);
        
        /* Verification */
        assertEquals(params, report.getParameters());
        assertEquals(LocalDate.now(), report.getGenerationDate());
        assertEquals(results, report.getResults());
        // The toString() format is unspecified, but at least ensure it is non-empty.
        assertThat(report.toString(), not(isEmptyOrNullString()));
    }
    
}

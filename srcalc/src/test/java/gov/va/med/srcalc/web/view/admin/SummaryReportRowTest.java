package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit tests for {@link SummaryReportRow}.
 */
public class SummaryReportRowTest
{
    @Test
    public final void testFromSignedResult()
    {
        final SignedResult signedResult = SampleCalculations.signedThoracic();
        final ImmutableList<SummaryReportRow> expectedRows = ImmutableList.of(
                new SummaryReportRow(signedResult, "Thoracic 30-Day"),
                new SummaryReportRow(signedResult, "Thoracic 90-Day"));

        /* Behavior */
        final ImmutableList<SummaryReportRow> actualRows =
                SummaryReportRow.fromSignedResult(signedResult);
        
        /* Verification */
        assertEquals(expectedRows, actualRows);
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(SummaryReportRow.class).verify();
    }
    
    // TODO: test toString()
    
}

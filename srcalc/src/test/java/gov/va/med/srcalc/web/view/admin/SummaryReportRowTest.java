package gov.va.med.srcalc.web.view.admin;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
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
                new SummaryReportRow(signedResult, SampleCalculations.THORACIC_MODEL_30_DAY),
                new SummaryReportRow(signedResult, SampleCalculations.THORACIC_MODEL_90_DAY));

        /* Behavior */
        final ImmutableList<SummaryReportRow> actualRows =
                SummaryReportRow.fromSignedResult(signedResult);
        
        /* Verification */
        assertEquals(expectedRows, actualRows);
        // Verify accessor values.
        final SummaryReportRow row1 = actualRows.get(0);
        assertEquals(signedResult.getCptCode().get(), row1.getCptCode());
        assertEquals(SampleCalculations.THORACIC_MODEL_30_DAY, row1.getRiskModelName());
        assertEquals(
                signedResult.getOutcomes().get(SampleCalculations.THORACIC_MODEL_30_DAY),
                row1.getOutcome(),
                0.0f);
        assertEquals(
                signedResult.getHistoricalCalculation().getProviderType().get(),
                row1.getProviderType());
        assertEquals(
                signedResult.getSignatureTimestamp(),
                row1.getSignatureTimestamp());
        assertEquals(
                signedResult.getHistoricalCalculation().getSpecialtyName(),
                row1.getSpecialtyName());
        assertEquals(
                signedResult.getHistoricalCalculation().getUserStation(),
                row1.getUserStation());
        // The toString() format is unspecified, but at least ensure it is non-empty.
        assertThat(row1.toString(), not(isEmptyOrNullString()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testInvalidModelName()
    {
        final SignedResult signedResult = SampleCalculations.signedThoracic();
        new SummaryReportRow(signedResult, "foo bar");
    }
    
    @Test(expected = NullPointerException.class)
    public final void testNullSignedResult()
    {
        new SummaryReportRow(null, "foo bar");
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(SummaryReportRow.class).verify();
    }
    
}

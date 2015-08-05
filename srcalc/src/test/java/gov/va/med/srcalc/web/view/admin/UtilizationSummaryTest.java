package gov.va.med.srcalc.web.view.admin;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.calculation.HistoricalCalculation;
import gov.va.med.srcalc.domain.calculation.HistoricalRunInfo;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Unit tests for the {@link UtilizationSummary} class.
 */
public class UtilizationSummaryTest
{
    /**
     * Constructs a {@link HistoricalCalculation} with specified values for the properties
     * we care about. Other properties are arbitrary.
     */
    private HistoricalCalculation makeHistoricalCalc(final int secondsToFirstRun)
    {
        return new HistoricalCalculation(
                "specialty",
                "442",
                new DateTime(2014, 02, 02, 10, 21),
                secondsToFirstRun,
                Optional.of("provider type"));
    }
    
    /**
     * Constructs a {@link SignedResult} with specified values for the properties we care
     * about. Other properties are arbitrary.
     */
    private SignedResult makeSignedResult(
            final int secondsToFirstRun, final int secondsToSign)
    {
        final HistoricalCalculation calc = makeHistoricalCalc(secondsToFirstRun);
        return new SignedResult(
                calc,
                100,
                Optional.<String>absent(),
                calc.getStartTimestamp().plusSeconds(secondsToSign),
                ImmutableMap.<String, String>of(),
                ImmutableMap.<String, Float>of());
    }

    @Test
    public final void testFromRunInfos()
    {
        final SignedResult result1 = makeSignedResult(40, 120);
        final SignedResult result2 = makeSignedResult(100, 100);
        final HistoricalCalculation calc3 = makeHistoricalCalc(70);
        final ImmutableList<HistoricalRunInfo> runInfos = ImmutableList.of(
                HistoricalRunInfo.signed(result1),
                HistoricalRunInfo.signed(result2),
                HistoricalRunInfo.unsigned(calc3));
        
        final int expectedTotal = runInfos.size();
        final int expectedSigned = 2;
        final int expectedFirstRunAvg = 70;
        final int expectedSignAvg = 110;
        final UtilizationSummary actualSummary = UtilizationSummary.fromRunInfos(runInfos);
        
        /* Behavior & Verification */
        assertEquals(expectedTotal, actualSummary.getTotalCount());
        assertEquals(expectedSigned, actualSummary.getSignedCount());
        assertEquals(expectedFirstRunAvg, actualSummary.getSecondsToFirstRunAverage());
        assertEquals(expectedSignAvg, actualSummary.getSecondsToSignAverage());
        // Also verify toString() contract.
        assertThat(actualSummary.toString(), allOf(
                containsString(Integer.toString(expectedTotal)),
                containsString(Integer.toString(expectedSigned)),
                containsString(Integer.toString(expectedFirstRunAvg)),
                containsString(Integer.toString(expectedSignAvg))));
    }
    
    @Test
    public final void testNoSigned()
    {
        final HistoricalCalculation calc = makeHistoricalCalc(67);
        final ImmutableList<HistoricalRunInfo> runInfos = ImmutableList.of(
                HistoricalRunInfo.unsigned(calc));
        
        final UtilizationSummary actualSummary = UtilizationSummary.fromRunInfos(runInfos);
        
        assertEquals(runInfos.size(), actualSummary.getTotalCount());
        assertEquals(0, actualSummary.getSignedCount());
        assertEquals(calc.getSecondsToFirstRun(), actualSummary.getSecondsToFirstRunAverage());
        assertEquals(-1, actualSummary.getSecondsToSignAverage());
    }
    
    /**
     * Tests behavior if we pass in an empty collection of HistoricalRunInfos. Definitely
     * an edge case, but worth testing.
     */
    @Test
    public final void testNoCalculations()
    {
        final ImmutableList<HistoricalRunInfo> runInfos = ImmutableList.of();

        final UtilizationSummary actualSummary = UtilizationSummary.fromRunInfos(runInfos);
        
        assertEquals(runInfos.size(), actualSummary.getTotalCount());
        assertEquals(0, actualSummary.getSignedCount());
        assertEquals(-1, actualSummary.getSecondsToFirstRunAverage());
        assertEquals(-1, actualSummary.getSecondsToSignAverage());
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(UtilizationSummary.class).verify();
    }
    
}

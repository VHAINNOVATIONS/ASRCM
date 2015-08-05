package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.db.HistoricalSearchParameters;
import gov.va.med.srcalc.domain.calculation.HistoricalCalculation;
import gov.va.med.srcalc.domain.calculation.HistoricalRunInfo;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.util.SearchResults;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Unit tests for the {@link UtilizationReport} class.
 */
public class UtilizationReportTest
{
    /**
     * Constructs a {@link HistoricalCalculation} with specified values for the properties
     * we care about. Other properties are arbitrary.
     */
    private HistoricalCalculation makeHistoricalCalc(
            final String specialty, final int secondsToFirstRun)
    {
        return new HistoricalCalculation(
                specialty,
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
            final String specialty, final int secondsToFirstRun, final int secondsToSign)
    {
        final HistoricalCalculation calc = makeHistoricalCalc(specialty, secondsToFirstRun);
        return new SignedResult(
                calc,
                100,
                Optional.<String>absent(),
                calc.getStartTimestamp().plusSeconds(secondsToSign),
                ImmutableMap.<String, String>of(),
                ImmutableMap.<String, Float>of());
    }

    @Test
    public final void testGetSpecialtySummaries()
    {
        /* Setup */
        final String specialty1 = "One";
        final int firstRun1 = 40;
        final int signed1 = 60;
        final SignedResult result1 = makeSignedResult(specialty1, firstRun1, signed1);
        final String specialty2 = "Two";
        final int firstRun2 = 111;
        final int signed2 = 222;
        final SignedResult result2 = makeSignedResult(specialty2, firstRun2, signed2);
        final int firstRun3 = 100;
        final int signed3 = 70;
        // Put this one in the same specialty as result1.
        final SignedResult result3 = makeSignedResult(specialty1, firstRun3, signed3);
        final ImmutableList<HistoricalRunInfo> runInfos = ImmutableList.of(
                HistoricalRunInfo.signed(result1),
                HistoricalRunInfo.signed(result2),
                HistoricalRunInfo.signed(result3));
        final ImmutableMap<String, UtilizationSummary> expectedSummaries = ImmutableMap.of(
                specialty1,
                new UtilizationSummary(2, 2, 70, 65),
                specialty2,
                new UtilizationSummary(1, 1, firstRun2, signed2));
        
        /* Behavior & Verification */
        final UtilizationReport actualReport = new UtilizationReport(
                new HistoricalSearchParameters(), new SearchResults<>(runInfos, false));
        assertEquals(expectedSummaries, actualReport.getSpecialtySummaries());
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(UtilizationReport.class).verify();
    }
}

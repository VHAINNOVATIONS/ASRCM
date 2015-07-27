package gov.va.med.srcalc.domain.calculation;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.SampleModels;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

/**
 * Tests the {@link SignedResult} class.
 */
public class SignedResultTest
{
    /**
     * Constructs a {@link HistoricalCalculation} with dummy values. Useful when we need
     * an instance but don't care what it contains.
     */
    private HistoricalCalculation makeHistoricalCalc()
    {
        return new HistoricalCalculation(
                "Dummy", "442", DateTime.now(), 123, Optional.of("Provider Type"));
    }

    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(SignedResult.class).verify();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testBadCptCode()
    {
        new SignedResult(
                makeHistoricalCalc(),
                500,
                Optional.of("4444"),
                DateTime.now(),
                ImmutableMap.<String, String>of(),
                ImmutableMap.<String, Float>of());
    }
    
    /**
     * Tests that {@link SignedResult#toString()} contains some basic information
     * to make it useful.
     */
    @Test
    public final void testToString()
    {
        final HistoricalCalculation historical = makeHistoricalCalc();
        final int patientDfn = 100;
        final String cptCode = SampleModels.repairLeftProcedure().getCptCode();

        final SignedResult result = new SignedResult(
                historical,
                patientDfn,
                Optional.of(cptCode),
                DateTime.now(),
                ImmutableMap.<String, String>of(),
                ImmutableMap.<String, Float>of());
        
        assertThat(result.toString(), allOf(
                containsString(historical.toString()),
                containsString(cptCode),
                containsString(String.valueOf(patientDfn))));
    }
    
}

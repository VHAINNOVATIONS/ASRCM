package gov.va.med.srcalc.domain.calculation;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.Procedure;
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
                "Dummy",
                "442",
                DateTime.now(),
                123,
                Optional.of("Provider Type"));
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
    
    @Test(expected = IllegalArgumentException.class)
    public final void testBadSignatureTimestamp()
    {
        final HistoricalCalculation historical = makeHistoricalCalc();
        new SignedResult(
                historical,
                500,
                Optional.of(SampleModels.repairRightProcedure().getCptCode()),
                historical.getStartTimestamp().minusSeconds(1),
                ImmutableMap.<String, String>of(),
                ImmutableMap.<String, Float>of());
    }
    
    /**
     * Tests basic accessors and that {@link SignedResult#toString()} contains some basic
     * information to make it useful.
     */
    @Test
    public final void testBasic()
    {
        final HistoricalCalculation historical = makeHistoricalCalc();
        final int patientDfn = 100;
        final Procedure procedure = SampleModels.repairLeftProcedure();
        final ImmutableMap<String, String> inputs =
                ImmutableMap.of("procedure", procedure.getShortString());
        final ImmutableMap<String, Float> outcomes =
                ImmutableMap.of("Thoracic 30-Day Mortality", 87.5f);
        final int secondsToSignature = 67;
        final DateTime signatureTimestamp =
                historical.getStartTimestamp().plusSeconds(secondsToSignature);

        final SignedResult result = new SignedResult(
                historical,
                patientDfn,
                Optional.of(procedure.getCptCode()),
                signatureTimestamp,
                inputs,
                outcomes);
        
        assertSame(historical, result.getHistoricalCalculation());
        assertEquals(patientDfn, result.getPatientDfn());
        assertEquals(Optional.of(procedure.getCptCode()), result.getCptCode());
        assertEquals(procedure.getCptCode(), result.getCptCodeNullable());
        assertEquals(signatureTimestamp, result.getSignatureTimestamp());
        assertEquals(secondsToSignature, result.getSecondsToSign());
        assertEquals(inputs, result.getInputs());
        assertEquals(outcomes, result.getOutcomes());
        assertThat(result.toString(), allOf(
                containsString(historical.toString()),
                containsString(procedure.getCptCode()),
                containsString(String.valueOf(patientDfn))));
    }
    
}

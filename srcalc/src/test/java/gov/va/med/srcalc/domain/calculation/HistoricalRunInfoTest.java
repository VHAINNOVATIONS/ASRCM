package gov.va.med.srcalc.domain.calculation;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * Unit tests for {@link HistoricalRunInfo}.
 */
public class HistoricalRunInfoTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(HistoricalRunInfo.class).verify();
    }
    
    @Test
    public final void testUnsigned()
    {
        final HistoricalCalculation calc = SampleCalculations.historicalThoracic();
        final HistoricalRunInfo runInfo = HistoricalRunInfo.unsigned(calc);
        
        assertSame(calc, runInfo.getHistoricalCalculation());
        assertFalse(runInfo.isSigned());
        assertEquals(Optional.<SignedResult>absent(), runInfo.getSignedResult());
        assertSame(null, runInfo.getSignedResultNullable());
        // We don't specify the format of toString() at all, but at least make sure it
        // is non-empty.
        assertThat(runInfo.toString(), not(isEmptyOrNullString()));
    }
    
    @Test
    public final void testSigned()
    {
        final SignedResult result = SampleCalculations.signedThoracic();
        final HistoricalRunInfo runInfo = HistoricalRunInfo.signed(result);
        
        assertSame(result.getHistoricalCalculation(), runInfo.getHistoricalCalculation());
        assertTrue(runInfo.isSigned());
        assertEquals(Optional.of(result), runInfo.getSignedResult());
        assertSame(result, runInfo.getSignedResultNullable());
    }
    
}

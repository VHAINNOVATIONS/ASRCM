package gov.va.med.srcalc.domain.calculation;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * Unit tests for {@link HistoricalCalculation}.
 */
public class HistoricalCalculationTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(HistoricalCalculation.class);
    }
    
}

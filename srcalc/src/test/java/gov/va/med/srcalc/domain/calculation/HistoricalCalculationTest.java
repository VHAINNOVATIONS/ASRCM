package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.test.util.TestHelpers;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.base.Optional;

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
    
    @Test(expected = IllegalArgumentException.class)
    public final void testProviderTypeTooLong()
    {
        new HistoricalCalculation(
                "specialty", "512", DateTime.now(), 1, Optional.of(
                        TestHelpers.stringOfLength(HistoricalCalculation.PROVIDER_TYPE_MAX + 1)));
        
    }
    
}

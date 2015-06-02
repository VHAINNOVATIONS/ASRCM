package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.NumericalRange;

import org.junit.Test;

public class NumericalRangeBuilderTest
{
    @Test
    public final void testBuild()
    {
        final NumericalRangeBuilder builder = new NumericalRangeBuilder();
        
        // Verify the defaults.
        assertEquals(
                new NumericalRange(0.0f, true, 100.0f, true),
                builder.build());
        
        // Now verify an entirely different range.
        final float lowerBound = -54.3f;
        final boolean lowerInclusive = false;
        final float upperBound = 2000.0f;
        final boolean upperInclusive = false;
        
        // Test all setters and getters
        builder.setLowerBound(lowerBound);
        assertEquals(lowerBound, builder.getLowerBound(), 0.0f);
        builder.setLowerInclusive(lowerInclusive);
        assertEquals(lowerInclusive, builder.getLowerInclusive());
        builder.setUpperBound(upperBound);
        assertEquals(upperBound, builder.getUpperBound(), 0.0f);
        builder.setUpperInclusive(upperInclusive);
        assertEquals(upperInclusive, builder.getUpperInclusive());
        
        // And test construction
        assertEquals(
                new NumericalRange(lowerBound, lowerInclusive, upperBound, upperInclusive),
                builder.build());
    }
    
}

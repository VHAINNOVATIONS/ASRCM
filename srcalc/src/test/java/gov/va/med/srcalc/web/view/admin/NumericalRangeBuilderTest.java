package gov.va.med.srcalc.web.view.admin;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
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
    
    @Test
    public final void testWithInitialValues()
    {
        final float lowerBound = -2.0f;
        final boolean lowerInclusive = false;
        final float upperBound = 2.0f;
        final boolean upperInclusive = false;
        final NumericalRangeBuilder builder = new NumericalRangeBuilder(
                lowerBound, lowerInclusive, upperBound, upperInclusive);
        
        assertEquals(
                new NumericalRange(lowerBound, lowerInclusive, upperBound, upperInclusive),
                builder.build());
        
        // And test toString()
        assertThat(builder.toString(), allOf(
                containsString(Float.toString(lowerBound)),
                containsString(Float.toString(upperBound)),
                containsString(Boolean.toString(lowerInclusive)),
                containsString(Boolean.toString(upperInclusive))));

    }
    
    @Test
    public final void testFromPrototype()
    {
        final NumericalRange prototype = new NumericalRange(-1.0f, false, 1.0f, true);
        final NumericalRangeBuilder builder = NumericalRangeBuilder.fromPrototype(prototype);
        
        assertEquals(prototype, builder.build());
    }
    
}

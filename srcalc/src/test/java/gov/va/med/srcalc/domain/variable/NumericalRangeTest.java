package gov.va.med.srcalc.domain.variable;

import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class NumericalRangeTest
{
    @Test
    public final void testIsValueInRange()
    {
        final NumericalRange range1 = new NumericalRange(1.0f, true, 100.1f, false);
        assertFalse("below range1", range1.isValueInRange(0.9f));
        assertTrue("range1 lower bound", range1.isValueInRange(1.0f));
        assertTrue("range1 upper bound", range1.isValueInRange(100.0f));
        assertFalse("above range1", range1.isValueInRange(100.1f));
        
        final NumericalRange range2 = new NumericalRange(5.0f, false, 50.2f, true);
        assertFalse("below range2", range2.isValueInRange(5.0f));
        assertTrue("range2 lower bound", range2.isValueInRange(5.01f));
        assertTrue("range2 upper bound", range2.isValueInRange(50.2f));
        assertFalse("above range2", range2.isValueInRange(50.21f));
    }
    
    @Test
    public final void testToString()
    {
        assertEquals(
                "[1.0, 100.1)",
                new NumericalRange(1.0f, true, 100.1f, false).toString());
        assertEquals(
                "(5.0, 50.2]",
                new NumericalRange(5.0f, false, 50.2f, true).toString());
    }
    
    @Test
    public final void testInfiniteLowerBound()
    {
        final NumericalRange r = new NumericalRange(
                Float.NEGATIVE_INFINITY, false, 5.0f, true);
        assertEquals(NumericalRange.MIN, r.getLowerBound(), 0.0f);
    }
    
    @Test
    public final void testInfiniteUpperBound()
    {
        final NumericalRange r = new NumericalRange(
                Float.POSITIVE_INFINITY, false, 5.0f, true);
        assertEquals(NumericalRange.MAX, r.getLowerBound(), 0.0f);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testNaNLowerBound()
    {
        new NumericalRange(Float.NaN, true, 0.0f, false);
    }
    
    @Test
    public final void testNegMaxValueLowerBound()
    {
        final NumericalRange r = new NumericalRange(-Float.MAX_VALUE, true, 0.0f, false);
        assertEquals(NumericalRange.MIN, r.getLowerBound(), 0.0f);
    }
    
    @Test
    public final void testPosMaxValueLowerBound()
    {
        final NumericalRange r = new NumericalRange(Float.MAX_VALUE, true, 0.0f, false);
        assertEquals(NumericalRange.MAX, r.getLowerBound(), 0.0f);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testNaNUpperBound()
    {
        new NumericalRange(0.0f, true, Float.NaN, false);
    }
    
    @Test
    public final void testNegMaxUpperBound()
    {
        final NumericalRange r = new NumericalRange(0.0f, true, -Float.MAX_VALUE, false);
        assertEquals(NumericalRange.MIN, r.getUpperBound(), 0.0f);
    }
    
    @Test
    public final void testPosMaxUpperBound()
    {
        final NumericalRange r = new NumericalRange(0.0f, true, Float.MAX_VALUE, false);
        assertEquals(NumericalRange.MAX, r.getUpperBound(), 0.0f);
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(NumericalRange.class)
            .suppress(Warning.NONFINAL_FIELDS)  // NumericalRange is actually immutable
            .verify();
    }
}

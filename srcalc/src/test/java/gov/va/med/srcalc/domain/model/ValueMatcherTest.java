package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;

import org.junit.Test;

public class ValueMatcherTest
{
    @Test
    public final void testToString()
    {
        final ValueMatcher vm = new ValueMatcher(
                SampleObjects.dnrVariable(), "value == true");
        assertEquals("ValueMatcher on DNR: \"value == true\"", vm.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testInvalidExpression()
    {
        new ValueMatcher(SampleObjects.dnrVariable(), "value asdfjasdf true");
    }
}

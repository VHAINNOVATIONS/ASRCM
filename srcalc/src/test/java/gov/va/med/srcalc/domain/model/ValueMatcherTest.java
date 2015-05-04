package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValueMatcherTest
{
    @Test
    public final void testToString()
    {
        final ValueMatcher vm = new ValueMatcher(
                SampleModels.dnrVariable(), "value == true");
        assertEquals("ValueMatcher on DNR: \"value == true\"", vm.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testInvalidExpression()
    {
        new ValueMatcher(SampleModels.dnrVariable(), "value asdfjasdf true");
    }
}

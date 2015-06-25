package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.calculation.BooleanValue;

import org.junit.Test;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ValueMatcherTest
{
    @Test
    public final void testToString()
    {
        final ValueMatcher vm = new ValueMatcher(
                SampleModels.dnrVariable(), "value == true", true);
        assertEquals("ValueMatcher on DNR: \"value == true\"", vm.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testInvalidExpression()
    {
        new ValueMatcher(SampleModels.dnrVariable(), "value asdfjasdf true", true);
    }
    
    @Test
    public final void testEmptyExpression()
    {
        // Empty expressions are allowed, and default to true
        final ValueMatcher vm = new ValueMatcher(
                SampleModels.dnrVariable(), "", true);
        final StandardEvaluationContext ec = new StandardEvaluationContext();
        final BooleanValue value = new BooleanValue(SampleModels.dnrVariable(), false);
        assertTrue(vm.evaluate(ec, value));
    }
    
    @Test
    public final void testDisabled()
    {
        final ValueMatcher vm = new ValueMatcher(
                SampleModels.dnrVariable(), "", false);
        final StandardEvaluationContext ec = new StandardEvaluationContext();
        final BooleanValue value = new BooleanValue(SampleModels.dnrVariable(), false);
        assertTrue(vm.evaluate(ec, value));
    }
}

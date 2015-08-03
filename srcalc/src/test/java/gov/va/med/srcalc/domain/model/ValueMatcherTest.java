package gov.va.med.srcalc.domain.model;

import static gov.va.med.srcalc.domain.model.SampleModels.expression1;
import static gov.va.med.srcalc.domain.model.SampleModels.expression2;
import static org.junit.Assert.*;
import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.domain.calculation.BooleanValue;
import gov.va.med.srcalc.domain.calculation.DiscreteNumericalValue;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Tests the {@link ValueMatcher} class.
 */
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
    
    @Test(expected = NullPointerException.class)
    public final void testEmptyExpression()
    {
        // Empty expressions are only allowed if the value matcher is disabled.
        final ValueMatcher vm = new ValueMatcher(
                SampleModels.dnrVariable(), "", true);
        final StandardEvaluationContext ec = new StandardEvaluationContext();
        final BooleanValue value = new BooleanValue(SampleModels.dnrVariable(), false);
        vm.evaluate(ec, value);
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
    
    /**
     * Tests that {@link DiscreteNumericalValue}s work properly in ValueMatchers.
     */
    @Test
    public final void testDiscreteValue() throws Exception
    {
        final DiscreteNumericalVariable wbc = SampleModels.wbcVariable();
        final ValueMatcher wbcMatcher = new ValueMatcher(wbc, "#wbc == '>11.0'", true);
        
        final StandardEvaluationContext ec = new StandardEvaluationContext();
        final DiscreteNumericalValue valWnl = wbc.makeValue(10.0f);
        ec.setVariable("wbc", valWnl.getValue());
        assertFalse(wbcMatcher.evaluate(ec, valWnl));
        
        final DiscreteNumericalValue valHigh = wbc.makeValue(11.5f);
        ec.setVariable("wbc", valHigh.getValue());
        assertTrue(wbcMatcher.evaluate(ec, valHigh));
    }
    
    @Test(expected = ConfigurationException.class)
    public final void testBadExpression()
    {
        final ValueMatcher vm = new ValueMatcher(
                // Note single equals
                SampleModels.dnrVariable(), "#dnr = 1", true);
        final StandardEvaluationContext ec = new StandardEvaluationContext();
        final BooleanValue value = new BooleanValue(SampleModels.dnrVariable(), true);
        ec.setVariable("dnr", value.getValue());
        vm.evaluate(ec, value);
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(ValueMatcher.class)
            .withPrefabValues(
                    Variable.class,
                    SampleModels.ageVariable(),
                    SampleModels.dnrVariable())
            .withPrefabValues(Expression.class, expression1(), expression2())
            .suppress(Warning.NULL_FIELDS)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
}

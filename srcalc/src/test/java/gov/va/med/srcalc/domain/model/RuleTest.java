package gov.va.med.srcalc.domain.model;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static gov.va.med.srcalc.domain.model.SampleModels.expression1;
import static gov.va.med.srcalc.domain.model.SampleModels.expression2;
import gov.va.med.srcalc.domain.calculation.Value;
import gov.va.med.srcalc.util.MissingValuesException;

import java.util.Arrays;
import java.util.HashMap;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.springframework.expression.Expression;

import com.google.common.collect.ImmutableSet;

public class RuleTest
{
    @Test
    public final void testGetRequiredVariables()
    {
        final Rule rule = SampleModels.ageAndFsRule();
        
        final ImmutableSet<AbstractVariable> expectedVars = ImmutableSet.of(
                        SampleModels.ageVariable(),
                        SampleModels.functionalStatusVariable());
        
        assertEquals(expectedVars, rule.getRequiredVariables());
    }
    
    @Test
    public final void testAgeMultiplier() throws Exception
    {
        // Setup
        final String summandExpression = "#age * #coefficient";
        final HashMap<Variable, Value> values = new HashMap<>();
        final NumericalVariable ageVar = SampleModels.ageVariable();
        values.put(ageVar, ageVar.makeValue(25));
        final MultiSelectVariable fsVar = SampleModels.functionalStatusVariable();
        final ValueMatcher totallyDependentMatcher = new ValueMatcher(
                fsVar, "#functionalStatus == 'Totally dependent'", true);
        final ValueMatcher ageMatcher = new ValueMatcher(ageVar, "", false);
        final Rule rule = new Rule(
                Arrays.asList(totallyDependentMatcher, ageMatcher),
                summandExpression, true, "Age multiplier for Functional Status");
        
        // Behavior verification
        values.put(fsVar, fsVar.makeValue(fsVar.getOptions().get(2)));
        assertEquals(
                50.0f, 
                rule.apply(new Rule.EvaluationContext(2.0f, values)),
                0.0);
        
        values.put(fsVar, fsVar.makeValue(fsVar.getOptions().get(1)));
        assertEquals(
                0.0f,
                rule.apply(new Rule.EvaluationContext(2.0f, values)),
                0.0);
        
        // The Rule.toString() contract doesn't specify the format, but it says
        // it will contain the summand expression and the matchers.
        assertThat(rule.toString(), allOf(
                containsString(totallyDependentMatcher.toString()),
                containsString(ageMatcher.toString()),
                containsString(summandExpression)));
    }
    
    @Test
    public final void testWeightLoss() throws Exception
    {
        // Setup
        final VariableGroup group = SampleModels.demographicsVariableGroup();
        final NumericalVariable currWeight = new NumericalVariable("Weight", group, "weight");
        currWeight.setValidRange(new NumericalRange(0.0f, false, 1000f, false));
        final NumericalVariable weight6MoAgo = new NumericalVariable("Weight6MoAgo", group, "weight6MonthsAgo");
        weight6MoAgo.setValidRange(new NumericalRange(0.0f, false, 1000f, false));
        final ValueMatcher weight6MoAgoMatcher = new ValueMatcher(weight6MoAgo, "", false);
        final ValueMatcher weightLossMatcher = new ValueMatcher(currWeight, "#weight < #weight6MonthsAgo * 0.9", true);
        final Rule rule =
                new Rule(Arrays.asList(weight6MoAgoMatcher, weightLossMatcher),
                        "#coefficient", true, "Weight loss in past 6 months > 10%");
        
        // Behavior verification
        final HashMap<Variable, Value> values = new HashMap<>();
        values.put(weight6MoAgo, weight6MoAgo.makeValue(150));
        values.put(currWeight, currWeight.makeValue(100));
        assertEquals(
                3.0f,
                rule.apply(new Rule.EvaluationContext(3.0f, values)),
                0.0);
        values.put(currWeight, currWeight.makeValue(140));
        assertEquals(
                0.0f,
                rule.apply(new Rule.EvaluationContext(3.0f, values)),
                0.0f);
        values.clear();
        values.put(currWeight, currWeight.makeValue(140));
        assertEquals(
                0.0f,
                rule.apply(new Rule.EvaluationContext(3.0f, values)),
                0.0f);
    }
    
    @Test(expected=MissingValuesException.class)
    public final void testRequired() throws Exception
    {
        // Setup
        final VariableGroup group = SampleModels.demographicsVariableGroup();
        final NumericalVariable currWeight = new NumericalVariable("Weight", group, "weight");
        final NumericalVariable weight6MoAgo = new NumericalVariable("Weight6MoAgo", group, "weight6MonthsAgo");
        final ValueMatcher weight6MoAgoMatcher = new ValueMatcher(weight6MoAgo, "", false);
        final ValueMatcher weightLossMatcher = new ValueMatcher(currWeight, "value < #weight6MonthsAgo.value * 0.9", true);
        final Rule rule =
                new Rule(Arrays.asList(weight6MoAgoMatcher, weightLossMatcher),
                        "#coefficient", true, "Weight loss in past 6 months > 10%");
        rule.setBypassEnabled(true);
        // Behavior verification
        final HashMap<Variable, Value> values = new HashMap<>();
        values.put(currWeight, currWeight.makeValue(100));
        assertEquals(
                0.0f,
                rule.apply(new Rule.EvaluationContext(3.0f, values)),
                0.0);
        rule.setBypassEnabled(false);
        rule.apply(new Rule.EvaluationContext(3.0f, values));
    }

    
    @Test
    public final void testEquals() throws Exception
    {

        EqualsVerifier.forClass(Rule.class)
            // Provide expression instances since Expression is an interface
            .withPrefabValues(Expression.class, expression1(), expression2())
            // The interface does not permit null fields.
            .suppress(Warning.NULL_FIELDS)
            .verify();
    }
}

package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;
import static gov.va.med.srcalc.domain.SampleObjects.expression1;
import static gov.va.med.srcalc.domain.SampleObjects.expression2;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.util.CollectionUtils;
import gov.va.med.srcalc.util.MissingValuesException;

import java.util.Arrays;
import java.util.HashMap;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.springframework.expression.Expression;

public class RuleTest
{
    @Test
    public final void testGetRequiredVariables()
    {
        final NumericalVariable ageVar = SampleObjects.sampleAgeVariable();
        final MultiSelectVariable fsVar = SampleObjects.functionalStatusVariable();
        final ValueMatcher totallyDependentMatcher = new ValueMatcher(
                fsVar, "value == 'Totally dependent'");
        final ValueMatcher ageMatcher = new ValueMatcher(ageVar, "true");
        final Rule rule = new Rule(
                Arrays.asList(totallyDependentMatcher, ageMatcher),
                "#Age.value * #coefficient", true);
        
        assertEquals(
                CollectionUtils.hashSet(ageVar, fsVar),
                rule.getRequiredVariables());
    }
    
    @Test
    public final void testAgeMultiplier() throws Exception
    {
        // Setup
        final HashMap<Variable, Value> values = new HashMap<>();
        final NumericalVariable ageVar = SampleObjects.sampleAgeVariable();
        values.put(ageVar, ageVar.makeValue(25));
        final MultiSelectVariable fsVar = SampleObjects.functionalStatusVariable();
        final ValueMatcher totallyDependentMatcher = new ValueMatcher(
                fsVar, "value == 'Totally dependent'");
        final ValueMatcher ageMatcher = new ValueMatcher(ageVar, "true");
        final Rule rule = new Rule(
                Arrays.asList(totallyDependentMatcher, ageMatcher),
                "#age.value * #coefficient", true);
        
        // Behavior verification
        values.put(fsVar, fsVar.makeValue(fsVar.getOptions().get(2)));
        assertEquals(
                50.0, 
                rule.apply(new Rule.EvaluationContext(2.0f, values)),
                0.0);
        
        values.put(fsVar, fsVar.makeValue(fsVar.getOptions().get(1)));
        assertEquals(
                0.0,
                rule.apply(new Rule.EvaluationContext(2.0f, values)),
                0.0);
    }
    
    @Test
    public final void testWeightLoss() throws Exception
    {
        // Setup
        final VariableGroup group = SampleObjects.demographicsVariableGroup();
        final NumericalVariable currWeight = new NumericalVariable("Weight", group, "weight");
        final NumericalVariable weight6MoAgo = new NumericalVariable("Weight6MoAgo", group, "weight6MonthsAgo");
        final ValueMatcher weight6MoAgoMatcher = new ValueMatcher(weight6MoAgo, "true");
        final ValueMatcher weightLossMatcher = new ValueMatcher(currWeight, "value < #weight6MonthsAgo.value * 0.9");
        final Rule rule =
                new Rule(Arrays.asList(weight6MoAgoMatcher, weightLossMatcher), "#coefficient", false);
        
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
        final VariableGroup group = SampleObjects.demographicsVariableGroup();
        final NumericalVariable currWeight = new NumericalVariable("Weight", group, "weight");
        final NumericalVariable weight6MoAgo = new NumericalVariable("Weight6MoAgo", group, "weight6MonthsAgo");
        final ValueMatcher weight6MoAgoMatcher = new ValueMatcher(weight6MoAgo, "true");
        final ValueMatcher weightLossMatcher = new ValueMatcher(currWeight, "value < #weight6MonthsAgo.value * 0.9");
        final Rule rule =
                new Rule(Arrays.asList(weight6MoAgoMatcher, weightLossMatcher), "#coefficient", false);
        rule.setRequired(false);
        // Behavior verification
        final HashMap<Variable, Value> values = new HashMap<>();
        values.put(currWeight, currWeight.makeValue(100));
        assertEquals(
                0.0f,
                rule.apply(new Rule.EvaluationContext(3.0f, values)),
                0.0);
        rule.setRequired(true);
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

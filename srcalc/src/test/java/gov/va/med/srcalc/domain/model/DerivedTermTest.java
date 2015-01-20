package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.*;

import java.util.Arrays;
import java.util.HashMap;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class DerivedTermTest
{
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
        final DerivedTerm term = new DerivedTerm(
                2.0f,
                Arrays.asList(totallyDependentMatcher, ageMatcher),
                "#Age.value * #coefficient");
        
        // Behavior verification
        values.put(fsVar, fsVar.makeValue(fsVar.getOptions().get(2)));
        assertEquals(50.0, term.getSummand(values), 0.0f);
        
        values.put(fsVar, fsVar.makeValue(fsVar.getOptions().get(1)));
        assertEquals(0.0, term.getSummand(values), 0.0f);
    }
    
    @Test
    public final void testWeightLoss() throws Exception
    {
        // Setup
        final VariableGroup group = SampleObjects.demographicsVariableGroup();
        final NumericalVariable currWeight = new NumericalVariable("Weight", group);
        final NumericalVariable weight6MoAgo = new NumericalVariable("Weight6MoAgo", group);
        final ValueMatcher weight6MoAgoMatcher = new ValueMatcher(weight6MoAgo, "true");
        final ValueMatcher weightLossMatcher = new ValueMatcher(currWeight, "value < #Weight6MoAgo.value * 0.9");
        final DerivedTerm term = new DerivedTerm(3.0f, Arrays.asList(weight6MoAgoMatcher, weightLossMatcher), "#coefficient");
        
        // Behavior verification
        final HashMap<Variable, Value> values = new HashMap<>();
        values.put(weight6MoAgo, weight6MoAgo.makeValue(150));
        values.put(currWeight, currWeight.makeValue(100));
        assertEquals(3.0f, term.getSummand(values), 0.0f);
        values.put(currWeight, currWeight.makeValue(140));
        assertEquals(0.0f, term.getSummand(values), 0.0f);
    }
    
    @Test
    public final void testEquals()
    {
        final SpelExpressionParser parser = new SpelExpressionParser();
        final Expression e1 = parser.parseExpression("true");
        final Expression e2 = parser.parseExpression("false");
        EqualsVerifier.forClass(DerivedTerm.class)
            // Provide expression instances since Expression is an interface
            .withPrefabValues(Expression.class, e1, e2)
            // The class actually provides an immutable interface.
            .suppress(Warning.NONFINAL_FIELDS)
            // Does not permit nulls.
            .suppress(Warning.NULL_FIELDS)
            .verify();
    }
    
}

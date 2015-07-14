package gov.va.med.srcalc.domain.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static gov.va.med.srcalc.domain.model.SampleModels.expression1;
import static gov.va.med.srcalc.domain.model.SampleModels.expression2;
import gov.va.med.srcalc.domain.calculation.Value;

import java.util.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.springframework.expression.Expression;

import com.google.common.collect.ImmutableSet;

public class DerivedTermTest
{
    @Test
    public final void testGetSummand() throws Exception
    {
        // setup
        final HashMap<Variable, Value> values = new HashMap<>();
        // We actually exercise the Rule in RuleTest, so just test simple
        // math here.
        final Rule rule = new Rule(
                Collections.<ValueMatcher>emptyList(),
                "#coefficient * 2",
                true,
                "Multiply Coefficient by 2");
        
        // behavior verification
        final float coeff1 = 56.1f;
        final DerivedTerm term1 = new DerivedTerm(coeff1, rule);
        assertEquals(coeff1 * 2, term1.getSummand(values), 0.001);
        
        // Test again wit a different term/coefficient.
        final float coeff2 = -1.7f;
        final DerivedTerm term2 = new DerivedTerm(coeff2, rule);
        assertEquals(coeff2 * 2, term2.getSummand(values), 0.001);
    }
    
    @Test
    public final void testBasic() throws Exception
    {
        // setup
        final Rule rule = SampleModels.ageAndFsRule();
        final ImmutableSet<AbstractVariable> expectedVars = ImmutableSet.of(
                        SampleModels.ageVariable(),
                        SampleModels.functionalStatusVariable());
        final float coeff = 9.1f;
        final DerivedTerm term = new DerivedTerm(coeff, rule);
        
        // behavior verification
        assertEquals(coeff, term.getCoefficient(), 0.0f);
        assertEquals(expectedVars, term.getRequiredVariables());
        // Ensure toString() contains some useful information.
        assertThat(term.toString(), containsString(rule.getDisplayName()));
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(DerivedTerm.class)
            // Provide expression instances since Expression is an interface
            .withPrefabValues(Expression.class, expression1(), expression2())
            // Relax the immutability rule.
            .suppress(Warning.NONFINAL_FIELDS)
            // Does not permit nulls.
            .suppress(Warning.NULL_FIELDS)
            .verify();
    }
}

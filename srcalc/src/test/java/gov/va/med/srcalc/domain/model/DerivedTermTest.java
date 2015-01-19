package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.*;

import java.util.Arrays;
import java.util.HashMap;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

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
    public final void testEquals()
    {
        EqualsVerifier.forClass(DerivedTerm.class)
            // The class actually provides an immutable interface.
            .suppress(Warning.NONFINAL_FIELDS)
            // Does not permit nulls.
            .suppress(Warning.NULL_FIELDS)
            .verify();
    }
    
}

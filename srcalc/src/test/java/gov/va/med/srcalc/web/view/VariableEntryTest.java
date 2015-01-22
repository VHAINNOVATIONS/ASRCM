package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.AbstractVariable;
import gov.va.med.srcalc.domain.variable.Variable;

import java.util.*;

import org.junit.Test;

public class VariableEntryTest
{
    @Test
    public final void testToString()
    {
        final VariableEntry entry = new VariableEntry(
                Collections.<Variable>emptyList());
        entry.getDynamicValues().put("one", "1");
        entry.getDynamicValues().put("two", "2");
        assertThat(
                entry.toString(),
                allOf(containsString("one=1"), containsString("two=2")));
    }
    
    @Test
    public final void testDefaults()
    {
        // Contains multiple variables, but only one DiscreteNumericalVariable.
        final List<AbstractVariable> vars = SampleObjects.sampleVariableList();
        
        // Behavior verification.
        final VariableEntry entry = new VariableEntry(vars);
        final HashMap<String, String> expected = new HashMap<>();
        expected.put(vars.get(5).getDisplayName(), VariableEntry.SPECIAL_NUMERICAL);
        assertEquals(expected, entry.getDynamicValues());
    }
}

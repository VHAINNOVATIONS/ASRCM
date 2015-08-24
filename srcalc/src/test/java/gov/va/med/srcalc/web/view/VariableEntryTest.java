package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.calculation.RetrievedValue;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.calculation.ValueRetrieverTest;
import gov.va.med.srcalc.domain.model.*;

import java.util.*;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Tests the {@link VariableEntry} class.
 */
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
        final List<AbstractVariable> vars = SampleModels.sampleVariableList();
        
        // Behavior verification.
        final VariableEntry entry = new VariableEntry(vars);
        final HashMap<String, String> expected = new HashMap<>();
        expected.put(vars.get(5).getKey(), VariableEntry.SPECIAL_NUMERICAL);
        assertEquals(expected, entry.getDynamicValues());
    }
    
    @Test
    public final void testPutDynamicValue()
    {
        final List<AbstractVariable> vars = SampleModels.sampleVariableList();
        
        final VariableEntry entry = new VariableEntry(vars);
        final AbstractVariable var = vars.get(0);
        final String value = "12";
        assertSame(entry, entry.putDynamicValue(var.getKey(), value));
        assertEquals(value, entry.getDynamicValues().get(var.getKey()));
    }
    
    /**
     * Tests population of some retrieved values. The complete set of {@link
     * ValueRetriever}s is tested in {@link ValueRetrieverTest}.
     */
    @Test
    public final void testWithRetrievedValues()
    {
        // Contains multiple variables, but only one DiscreteNumericalVariable.
        final List<AbstractVariable> vars = SampleModels.sampleVariableList();
        
        // Behavior verification.
        final Patient patient = SampleCalculations.dummyPatient(1);
        final VariableEntry entry = VariableEntry.withRetrievedValues(vars, patient);
        final HashMap<String, String> expected = new HashMap<>();
        expected.put(vars.get(1).getKey(), String.valueOf(patient.getAge()));
        expected.put(vars.get(2).getKey(), patient.getGender().name());
        expected.put(vars.get(5).getKey(), VariableEntry.SPECIAL_NUMERICAL);
        assertEquals(expected, entry.getDynamicValues());
    }
    
    @Test
    public final void testPutRetrievedValue()
    {
        /* Setup */
        final DiscreteNumericalVariable var =SampleModels.wbcVariable();
        final ImmutableSet<DiscreteNumericalVariable> vars = ImmutableSet.of(var);
        final DateTime measureDate = new DateTime(2014, 2, 27, 8, 21);
        final RetrievedValue sampleValue = new RetrievedValue(
                10.0, measureDate.toDate(), var.getUnits());
        final VariableEntry variableEntry = new VariableEntry(vars);
        
        /* Behavior & Verification */
        final String inputKey = VariableEntry.makeNumericalInputName(var.getKey());
        assertSame(variableEntry, variableEntry.putRetrievedValue(inputKey, sampleValue));
        assertEquals(
                String.valueOf(sampleValue.getValue()),
                variableEntry.getDynamicValues().get(inputKey));
        assertEquals(
                VariableEntry.makeRetrievalString(sampleValue),
                variableEntry.getMeasureDate(inputKey));
    }
}

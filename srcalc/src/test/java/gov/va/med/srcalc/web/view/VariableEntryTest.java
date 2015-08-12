package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.VistaLabs;
import gov.va.med.srcalc.domain.calculation.RetrievedValue;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.domain.model.*;

import java.util.*;

import org.junit.Test;

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
    public final void testWithRetrievedValues()
    {
        // Contains multiple variables, but only one DiscreteNumericalVariable.
        final List<AbstractVariable> vars = SampleModels.sampleVariableList();
        
        // Behavior verification.
        final Patient patient = SampleCalculations.dummyPatient(1);
        final VariableEntry entry = VariableEntry.withRetrievedValues(vars, patient);
        final HashMap<String, String> expected = new HashMap<>();
        expected.put(vars.get(1).getKey(), String.valueOf(patient.getAge()));
        expected.put(vars.get(2).getKey(), patient.getGender());
        expected.put(vars.get(5).getKey(), VariableEntry.SPECIAL_NUMERICAL);
        assertEquals(expected, entry.getDynamicValues());
    }
    
    @Test
    public final void testRetrievedCardiacAge()
    {
        final List<AbstractVariable> vars = SampleModels.sampleCardiacCABGVariableList();
        final Patient patient = SampleCalculations.dummyPatient(1);
        final VariableEntry entry = VariableEntry.withRetrievedValues(vars, patient);
        final HashMap<String, String> expected = new HashMap<>();
        expected.put(VariableEntry.makeNumericalInputName(vars.get(0).getKey()),
                String.valueOf(patient.getAge()));
        expected.put(vars.get(0).getKey(), VariableEntry.SPECIAL_NUMERICAL);
        expected.put(vars.get(1).getKey(), patient.getGender());
        
        assertEquals(expected, entry.getDynamicValues());
    }
    
    @Test
    public final void testWithRetrievedLabs()
    {
        final List<AbstractVariable> vars = SampleModels.sampleVariableList();
        final Patient patient = SampleCalculations.dummyPatientWithLabs(1);
        final VariableEntry entry = VariableEntry.withRetrievedValues(vars, patient);
        final HashMap<String, String> expected = new HashMap<>();
        
        expected.put(vars.get(1).getKey(), String.valueOf(patient.getAge()));
        expected.put(vars.get(2).getKey(), patient.getGender());
        expected.put(vars.get(5).getKey(), VariableEntry.SPECIAL_NUMERICAL);
        
        final RetrievedValue wbcLabValue = patient.getLabs().get(VistaLabs.WBC);
        
        final String wbcRetrievalString = VariableEntry.makeRetrievalString(
                wbcLabValue.getValue(),
                wbcLabValue.getMeasureDate(),
                wbcLabValue.getUnits());
        
        expected.put(VariableEntry.makeNumericalInputName(vars.get(5).getKey()),
                String.valueOf(wbcLabValue.getValue()));
        expected.put(
                VariableEntry.makeRetrievalString(
                        VariableEntry.makeNumericalInputName(vars.get(5).getKey())),
                wbcRetrievalString);
        
        assertEquals(expected, entry.getDynamicValues());
    }
}

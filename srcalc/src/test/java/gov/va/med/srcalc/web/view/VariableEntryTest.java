package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.VistaLabs;
import gov.va.med.srcalc.domain.calculation.RetrievedValue;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;

import java.util.*;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

/**
 * Tests the {@link VariableEntry} class.
 */
public class VariableEntryTest
{
    /**
     * A map where the key is the variable key and the value is the ValueRetriever belonging
     * to that variable.
     */
    private static final Map<String, ValueRetriever> LAB_MAP = ImmutableMap.<String, ValueRetriever>builder()
            .put("albumin", ValueRetriever.ALBUMIN)
            .put("alkalinePhostphatase", ValueRetriever.ALKALINE_PHOSPHATASE)
            .put("bilirubin", ValueRetriever.BILIRUBIN)
            .put("bun", ValueRetriever.BUN)
            .put("creatinine", ValueRetriever.CREATININE)
            .put("glucose", ValueRetriever.GLUCOSE)
            .put("hematocrit", ValueRetriever.HEMATOCRIT)
            .put("hga1c", ValueRetriever.HGA1C)
            .put("inr", ValueRetriever.INR)
            .put("platelets", ValueRetriever.PLATELETS)
            .put("potassium", ValueRetriever.POTASSIUM)
            .put("ptt", ValueRetriever.PTT)
            .put("sgot", ValueRetriever.SGOT)
            .put("sodium", ValueRetriever.SODIUM)
            .put("wbc", ValueRetriever.WBC)
            .build();
    
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
        final List<AbstractVariable> vars = new ArrayList<AbstractVariable>();
        final Patient patient = SampleCalculations.dummyPatientWithLabs(1);
        
        final HashMap<String, String> expected = new HashMap<>();
        
        addAllLabs(expected, vars, patient);
        final VariableEntry entry = VariableEntry.withRetrievedValues(vars, patient);
        assertEquals(expected, entry.getDynamicValues());
    }
    
    /**
     * This method adds all of the necessary information for the lab variables to the
     * expected map so the values can be compared later.
     * @param expected a map that describes what 
     * @param vars a list of AbstractVariables needed for the calculation
     * @param patient the patient for the current calculation
     */
    private void addAllLabs(final Map<String, String> expected, final List<AbstractVariable> vars,
            final Patient patient)
    {
        for(final String key: LAB_MAP.keySet())
        {
            expected.put(key, VariableEntry.SPECIAL_NUMERICAL);
            final ValueRetriever retriever = LAB_MAP.get(key);
            vars.add(makeLabVariable(key, retriever));
            final RetrievedValue labValue = patient.getLabs().get(VistaLabs.valueOf(retriever.name()));
            
            final String retrievalString = VariableEntry.makeRetrievalString(
                    labValue.getValue(),
                    labValue.getMeasureDate(),
                    labValue.getUnits());
            
            expected.put(VariableEntry.makeNumericalInputName(key),
                    String.valueOf(labValue.getValue()));
            expected.put(
                    VariableEntry.makeRetrievalString(
                            VariableEntry.makeNumericalInputName(key)),
                    retrievalString);
        }
    }
    
    /**
     * This method returns a DiscreteNumericalVariable with the specified key and retriever, but
     * with dummy information for the other fields in the variable.
     * @param key the key to use for the variable
     * @param retriever the ValueRetriever for this variable
     * @return the constructed DiscreteNumericalVariable
     */
    private DiscreteNumericalVariable makeLabVariable(final String key, final ValueRetriever retriever)
    {
        final Category wbcWnl = new Category(
                new MultiSelectOption("WNL"), 11.0f, true);
        final Category wbcHigh = new Category(
                new MultiSelectOption(">11.0"), Float.POSITIVE_INFINITY, false);
        final List<Category> categories = Arrays.asList(wbcWnl, wbcHigh);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "Generic Lab Display Name", SampleModels.labVariableGroup(), new HashSet<>(categories), key);
        var.setValidRange(new NumericalRange(2.0f, true, 50.0f, true));
        var.setUnits("generic units");
        var.setRetriever(retriever);
        return var;
    }
}

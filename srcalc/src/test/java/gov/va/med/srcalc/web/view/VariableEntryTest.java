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

/**
 * Tests the {@link VariableEntry} class.
 */
public class VariableEntryTest
{
    /**
     * A map where the key is the variable key and the value is the ValueRetriever belonging
     * to that variable.
     */
    private static final Map<String, ValueRetriever> LAB_MAP;
    static {
        LAB_MAP = new HashMap<String, ValueRetriever>();
        LAB_MAP.put("albumin", ValueRetriever.ALBUMIN);
        LAB_MAP.put("alkalinePhostphatase", ValueRetriever.ALKALINE_PHOSPHATASE);
        LAB_MAP.put("bilirubin", ValueRetriever.BILIRUBIN);
        LAB_MAP.put("bun", ValueRetriever.BUN);
        LAB_MAP.put("creatinine", ValueRetriever.CREATININE);
        LAB_MAP.put("glucose", ValueRetriever.GLUCOSE);
        LAB_MAP.put("hematocrit", ValueRetriever.HEMATOCRIT);
        LAB_MAP.put("hga1c", ValueRetriever.HGA1C);
        LAB_MAP.put("inr", ValueRetriever.INR);
        LAB_MAP.put("platelets", ValueRetriever.PLATELETS);
        LAB_MAP.put("potassium", ValueRetriever.POTASSIUM);
        LAB_MAP.put("ptt", ValueRetriever.PTT);
        LAB_MAP.put("sgot", ValueRetriever.SGOT);
        LAB_MAP.put("sodium", ValueRetriever.SODIUM);
        LAB_MAP.put("wbc", ValueRetriever.WBC);
    }
    
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

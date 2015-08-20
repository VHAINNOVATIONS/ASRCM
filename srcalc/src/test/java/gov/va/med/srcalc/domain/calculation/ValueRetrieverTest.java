package gov.va.med.srcalc.domain.calculation;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.VistaLabs;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.web.view.VariableEntry;

import java.util.*;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Tests the various {@link ValueRetriever}s. Note that we test via {@link VariableEntry}
 * because the former depends on it.
 */
public class ValueRetrieverTest
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
    
    /**
     * Constructs a {@link VariableEntry} with retrieved values from the given patient
     * and asserts that it contains the expected dynamic values.
     */
    private void verifyRetrievedValue(
            final Collection<? extends Variable> vars,
            final Patient patient,
            final Map<String, String> expectedValues)
    {
        final VariableEntry actualVariableEntry = VariableEntry.withRetrievedValues(
                vars, patient);
        assertEquals(expectedValues, actualVariableEntry.getDynamicValues());
    }
    
    /**
     * A simplification of the above {@link #verifyRetrievedValue(Collection, Patient,
     * Map)} to facilitate testing with a {@link RetrievedValue} object.
     */
    private void verifyRetrievedValue(
            final DiscreteNumericalVariable var,
            final Patient patient,
            final RetrievedValue retrievedValue)
    {
        final Collection<DiscreteNumericalVariable> vars = ImmutableSet.of(var);
        // Use VariableEntry to generate the expected dynamic values.
        final VariableEntry expected = new VariableEntry(vars).putRetrievedValue(
                        VariableEntry.makeNumericalInputName(var.getKey()),
                        retrievedValue);
        
        verifyRetrievedValue(vars, patient, expected.getDynamicValues());
    }
    
    @Test
    public final void testCardiacAge()
    {
        final DiscreteNumericalVariable var = SampleModels.cardiacAgeVariable();
        final Patient patient = SampleCalculations.dummyPatientWithVitals(1);
        final Collection<DiscreteNumericalVariable> vars = ImmutableSet.of(var);
        // Use VariableEntry to generate the expected dynamic values.
        final VariableEntry expected = new VariableEntry(vars).putDynamicValue(
                VariableEntry.makeNumericalInputName(var.getKey()),
                String.valueOf(patient.getAge()));

        verifyRetrievedValue(vars, patient, expected.getDynamicValues());
    }
    
    @Test
    public final void testBmi()
    {
        final DiscreteNumericalVariable bmiVar =
                makeDiscreteNumerical("bmi", ValueRetriever.BMI);
        final Patient patient = SampleCalculations.dummyPatientWithVitals(1);
        verifyRetrievedValue(bmiVar, patient, patient.getBmi());
    }
    
    @Test
    public final void testHeight()
    {
        final DiscreteNumericalVariable heightVar = 
                makeDiscreteNumerical("height", ValueRetriever.HEIGHT);
        final Patient patient = SampleCalculations.dummyPatientWithVitals(1);
        verifyRetrievedValue(heightVar, patient, patient.getHeight());
    }
    
    @Test
    public final void testWeight()
    {
        final DiscreteNumericalVariable weightVar =
                makeDiscreteNumerical("weight", ValueRetriever.WEIGHT);
        final Patient patient = SampleCalculations.dummyPatientWithVitals(1);
        verifyRetrievedValue(weightVar, patient, patient.getWeight());
    }
    
    @Test
    public final void testWeight6MonthsAgo()
    {
        final DiscreteNumericalVariable weight6MonthsAgoVar =
                makeDiscreteNumerical("weight6MonthsAgo", ValueRetriever.WEIGHT_6_MONTHS_AGO);
        final Patient patient = SampleCalculations.dummyPatientWithVitals(1);
        verifyRetrievedValue(weight6MonthsAgoVar, patient, patient.getWeight6MonthsAgo());
    }
    
    @Test
    public final void testWithRetrievedLabs()
    {
        final List<AbstractVariable> vars = new ArrayList<AbstractVariable>();
        final Patient patient = SampleCalculations.dummyPatientWithLabs(1);
        
        // Note that vars is empty at this point.
        final VariableEntry expected = new VariableEntry(vars);
        
        addAllLabs(expected, vars, patient);
        final VariableEntry entry = VariableEntry.withRetrievedValues(vars, patient);
        assertEquals(expected.getDynamicValues(), entry.getDynamicValues());
    }
    
    /**
     * This method adds all of the necessary information for the lab variables to the
     * expected map so the values can be compared later.
     * @param expected [out] to populate expected dynamic values
     * @param vars [out] a list of AbstractVariables needed for the calculation
     * @param patient the patient for the current calculation
     */
    private void addAllLabs(final VariableEntry expected, final List<AbstractVariable> vars,
            final Patient patient)
    {
        for(final String key: LAB_MAP.keySet())
        {
            // Warning: semantic coupling. Simluate what VariableEntry would do if we
            // constructed it using the variable list.
            expected.putDynamicValue(key, VariableEntry.SPECIAL_NUMERICAL);
            final ValueRetriever retriever = LAB_MAP.get(key);
            vars.add(makeDiscreteNumerical(key, retriever));
            final RetrievedValue labValue = patient.getLabs().get(VistaLabs.valueOf(retriever.name()));
            
            final String inputKey = VariableEntry.makeNumericalInputName(key);
            expected.putRetrievedValue(inputKey, labValue);
        }
    }
    
    /**
     * This method returns a DiscreteNumericalVariable with the specified key and retriever, but
     * with dummy information for the other fields in the variable.
     * @param key the key to use for the variable
     * @param retriever the ValueRetriever for this variable
     * @return the constructed DiscreteNumericalVariable
     */
    private DiscreteNumericalVariable makeDiscreteNumerical(final String key, final ValueRetriever retriever)
    {
        final Category wbcWnl = new Category(
                new MultiSelectOption("WNL"), 11.0f, true);
        final Category wbcHigh = new Category(
                new MultiSelectOption(">11.0"), Float.POSITIVE_INFINITY, false);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "Generic Display Name",
                SampleModels.labVariableGroup(),
                ImmutableSet.of(wbcWnl, wbcHigh),
                key);
        var.setValidRange(new NumericalRange(2.0f, true, 50.0f, true));
        var.setUnits("generic units");
        var.setRetriever(retriever);
        return var;
    }
}

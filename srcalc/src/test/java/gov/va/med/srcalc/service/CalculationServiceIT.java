package gov.va.med.srcalc.service;

import static org.junit.Assert.*;

import java.util.*;

import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.test.util.IntegrationTest;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;

/**
 * <p>Integration Test for {@link CalculationService}. Unlike
 * {@link DefaultCalculationServiceTest}, this class tests real database interaction.</p>
 * 
 * <p>Note that we are testing the interface ({@link CalculationService}), not the
 * implementation ({@link DefaultCalculationService}). You can think of this IT
 * as testing the Spring-instantiated service bean, not a particular Java class.</p>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/srcalc-context.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class CalculationServiceIT extends IntegrationTest
{
    @Inject // field-based autowiring only in tests
    CalculationService fCalculationService;
    
    @Test
    public void testGetValidSpecialties()
    {
        assertEquals(
                SampleModels.specialtyList(),
                fCalculationService.getValidSpecialties());
    }

    @Test
    public void testStartNewCalculation()
    {
        // Test setup and configuration
        final int PATIENT_DFN = 1;
        final DateTime testStartDateTime = new DateTime();
        
        // Behavior verification
        final Calculation calc = fCalculationService.startNewCalculation(
                PATIENT_DFN);
        assertEquals(PATIENT_DFN, calc.getPatient().getDfn());
        assertTrue("start date not in the past",
                // DateTime has millisecond precision, so the current time may
                // still be the same. Use "less than or equal to".
                calc.getStartDateTime().compareTo(new DateTime()) <= 0);
        assertTrue("start date not after test start",
                calc.getStartDateTime().compareTo(testStartDateTime) >= 0);
    }
    
    @Test
    public void testSetValidSpecialty() throws InvalidIdentifierException
    {
        final int PATIENT_DFN = 1;
        final Specialty sampleSpecialty = SampleModels.thoracicSpecialty();
        
        // Create the class under test.
        final Calculation calc = fCalculationService.startNewCalculation(PATIENT_DFN);
        
        // Behavior verification.
        fCalculationService.setSpecialty(calc, sampleSpecialty.getName());
        final Specialty actualSpecialty = calc.getSpecialty();
        assertEquals(sampleSpecialty, actualSpecialty);
        // Specialty.equals() does not compare the variables, so do some checks
        // there.
        final Set<Variable> specialtyVars = actualSpecialty.getModelVariables();
        assertEquals(specialtyVars, calc.getVariables());
        
        assertCleanSession();
    }
    
    /**
     * Builds a Map from variable key to variable.
     */
    private HashMap<String, Variable> buildVariableMap(Iterable<Variable> variables)
    {
        final HashMap<String, Variable> map = new HashMap<>();
        for (final Variable v : variables)
        {
            map.put(v.getKey(), v);
        }
        return map;
    }
    
    @Test
    public void testRunThoracicCalculation() throws Exception
    {
        final int PATIENT_DFN = 1;
        final Specialty specialty = SampleModels.thoracicSpecialty();
        
        // Create the class under test.
        final Calculation calc = fCalculationService.startNewCalculation(PATIENT_DFN);
        fCalculationService.setSpecialty(calc, specialty.getName());

        // Build a List of Values in the known order for Thoracic.
        final Map<String, Variable> thoracicVars = buildVariableMap(calc.getVariables());
        final ProcedureVariable procedureVar =
                (ProcedureVariable)thoracicVars.get("procedure");
        final Procedure selectedProcedure = procedureVar.getProcedures().get(1);
        final MultiSelectVariable asaVar =
                (MultiSelectVariable)thoracicVars.get("asaClassification");
        final DiscreteNumericalVariable apVar =
                (DiscreteNumericalVariable)thoracicVars.get("alkalinePhosphatase");
        final DiscreteNumericalVariable bunVar =
                (DiscreteNumericalVariable)thoracicVars.get("bun");
        final ImmutableSet<Value> expectedValues = ImmutableSet.of(
                ((NumericalVariable)thoracicVars.get("age")).makeValue(66),
                apVar.makeValue(11.0f),
                asaVar.makeValue(asaVar.getOptions().get(3)),
                ((NumericalVariable)thoracicVars.get("bmi")).makeValue(17.3f),
                bunVar.makeValue(50.0f),
                ((BooleanVariable)thoracicVars.get("dnr")).makeValue(true),
                ((BooleanVariable)thoracicVars.get("preopPneumonia")).makeValue(false),
                procedureVar.makeValue(selectedProcedure));
        
        // Calculate expected sum
        final TreeMap<String, Float> expectedOutcomes = new TreeMap<>();
        // These values and the configured coefficients are high enough to get 100% risk.
        expectedOutcomes.put("Thoracic 30-Day Mortality Risk", 1.0f);
        
        // Behavior verification
        final CalculationResult result =
                fCalculationService.runCalculation(calc, expectedValues);
        assertEquals(expectedValues, result.getValues());
        assertEquals(expectedOutcomes, result.getOutcomes());
    }
}

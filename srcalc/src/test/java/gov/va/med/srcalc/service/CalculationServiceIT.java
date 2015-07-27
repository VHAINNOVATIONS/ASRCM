package gov.va.med.srcalc.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.test.util.TestAuthnProvider;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
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
    
    @Rule
    public final TestAuthnProvider fAuthnProvider = new TestAuthnProvider();
    
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
        final DateTime testStartDateTime = DateTime.now();
        
        // Behavior verification
        final Calculation calc = fCalculationService.startNewCalculation(
                PATIENT_DFN);
        assertEquals(PATIENT_DFN, calc.getPatient().getDfn());
        assertTrue("start date should be in the past",
                // DateTime has millisecond precision, so the current time may
                // still be the same. Use "less than or equal to".
                calc.getStartDateTime().compareTo(DateTime.now()) <= 0);
        assertTrue("start date should be after test start",
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
        
        // These values and the configured coefficients are high enough to get 100% risk.
        final ImmutableMap<String, Float> expectedOutcomes = ImmutableMap.of(
                "Thoracic 30-Day Mortality Risk", 1.0f);
        
        // Behavior verification
        final CalculationResult result =
                fCalculationService.runCalculation(calc, expectedValues);
        assertEquals(expectedValues, result.getValues());
        assertEquals(expectedOutcomes, result.getOutcomes());
    }
    
    @Test
    public final void testSignCalculation() throws Exception
    {
        /* Setup */

        final int patientDfn = 76345;
        final String procedureKey = "procedure";
        final String ageKey = "age";

        // Create a Calculation just for easy access to the variabes.
        final Calculation calc = fCalculationService.startNewCalculation(patientDfn);
        fCalculationService.setSpecialty(calc, "Neurosurgery");
        final Map<String, Variable> neuroVars = buildVariableMap(calc.getVariables());

        final ProcedureVariable procedureVar =
                (ProcedureVariable)neuroVars.get(procedureKey);
        final Procedure procedure = procedureVar.getProcedures().get(1);
        final NumericalVariable ageVar = (NumericalVariable)neuroVars.get(ageKey);
        final HistoricalCalculation historical = new HistoricalCalculation(
                "Neurosurgery",
                "442",
                DateTime.now().minusMinutes(2),
                60,
                Optional.<String>absent());
        final ImmutableSet<Value> values = ImmutableSet.of(
                procedureVar.makeValue(procedure),
                ageVar.makeValue(41.0f));
        final ImmutableMap<String, Float> outcomes = ImmutableMap.of(
                "Neurosurgery 30-Day", 34.1f);
        final CalculationResult result = new CalculationResult(
                historical, patientDfn, DateTime.now(), values, outcomes);
        
        /* Behavior */
        fCalculationService.signRiskCalculation(result, "doesntmatter");
        
        /* Verification */
        simulateNewSession();

        assertEquals(
                1, getHibernateSession().createQuery("from SignedResult").list().size());
    }
}

package gov.va.med.srcalc.service;

import static org.junit.Assert.*;

import java.util.*;

import gov.va.med.srcalc.domain.*;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.domain.workflow.NewCalculation;
import gov.va.med.srcalc.domain.workflow.SelectedCalculation;
import gov.va.med.srcalc.test.util.IntegrationTest;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
    public void testStartNewCalculation()
    {
        // Test setup and configuration
        final int PATIENT_DFN = 1;
        final DateTime testStartDateTime = new DateTime();
        
        // Behavior verification
        final NewCalculation newCalc = fCalculationService.startNewCalculation(
                PATIENT_DFN);
        final Calculation calc = newCalc.getCalculation();
        assertEquals(PATIENT_DFN, calc.getPatient().getDfn());
        assertTrue("start date not in the past",
                // DateTime has millisecond precision, so the current time may
                // still be the same. Use "less than or equal to".
                calc.getStartDateTime().compareTo(new DateTime()) <= 0);
        assertTrue("start date not after test start",
                calc.getStartDateTime().compareTo(testStartDateTime) >= 0);
        assertEquals(SampleObjects.sampleSpecialtyList(), newCalc.getPossibleSpecialties());
        // TODO: other aspects of the calculation as we determine them
    }
    
    @Test
    public void testSetValidSpecialty() throws InvalidIdentifierException
    {
        final int PATIENT_DFN = 1;
        final Specialty sampleSpecialty = SampleObjects.sampleThoracicSpecialty();
        
        // Create the class under test.
        final NewCalculation newCalc = fCalculationService.startNewCalculation(PATIENT_DFN);
        final Calculation calc = newCalc.getCalculation();
        
        // Behavior verification.
        final SelectedCalculation selCalc =
                fCalculationService.setSpecialty(calc, sampleSpecialty.getName());
        assertSame("Calculation object not the same", calc,  selCalc.getCalculation());
        final Specialty actualSpecialty = selCalc.getCalculation().getSpecialty();
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
        final Specialty specialty = SampleObjects.sampleThoracicSpecialty();
        
        // Create the class under test.
        final NewCalculation newCalc = fCalculationService.startNewCalculation(PATIENT_DFN);
        final Calculation calc = newCalc.getCalculation();
        final SelectedCalculation selCalc =
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
        final float age = 66;
        final float bmi = 17.3f;
        // Construct this list in order by variable display name.
        final List<Value> orderedValues = Arrays.asList(
                ((NumericalVariable)thoracicVars.get("age")).makeValue(age),
                apVar.makeValue(11.0f),
                asaVar.makeValue(asaVar.getOptions().get(3)),
                ((NumericalVariable)thoracicVars.get("bmi")).makeValue(bmi),
                bunVar.makeValue(50.0f),
                ((BooleanVariable)thoracicVars.get("dnr")).makeValue(true),
                ((BooleanVariable)thoracicVars.get("preopPneumonia")).makeValue(false),
                procedureVar.makeValue(selectedProcedure));
        
        // Create a new shuffled list to test Calculation's sorting.
        final List<Value> shuffledValues = new ArrayList<>(orderedValues);
        Collections.shuffle(shuffledValues);
        
        // Calculate expected sum
        final double expectedSum =
                -4.0 + 
                2.4 * age +
                // 0 for Alkaline Phosphatase
                8.4 + // ASA Classification = 5
                5.4 * bmi +
                10.4 + // BUN > 25
                6.4 +  // DNR
                // 0 for Preop Pneumonia
                3.4 * selectedProcedure.getRvu();
        final double expectedExp = Math.exp(expectedSum);
        final double expectedResult = expectedExp / (1 + expectedExp);
        final TreeMap<String, Double> expectedOutcomes = new TreeMap<>();
        expectedOutcomes.put("Thoracic 30-Day Mortality Risk", expectedResult);
        
        // Behavior verification
        fCalculationService.runCalculation(selCalc.getCalculation(), orderedValues);
        assertEquals(orderedValues, new ArrayList<>(calc.getValues()));
        assertEquals(expectedOutcomes, selCalc.getCalculation().getOutcomes());
    }
}

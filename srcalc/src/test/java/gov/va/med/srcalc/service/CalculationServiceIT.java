package gov.va.med.srcalc.service;

import static org.junit.Assert.*;

import java.util.*;

import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.Specialty;
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
     * Builds a Map from variable name to variable.
     */
    private HashMap<String, Variable> buildVariableMap(Iterable<Variable> variables)
    {
        final HashMap<String, Variable> map = new HashMap<>();
        for (final Variable v : variables)
        {
            map.put(v.getDisplayName(), v);
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
                (ProcedureVariable)thoracicVars.get("Procedure");
        final MultiSelectVariable asaVar =
                (MultiSelectVariable)thoracicVars.get("ASA Classification");
        final DiscreteNumericalVariable apVar =
                (DiscreteNumericalVariable)thoracicVars.get("Alkaline Phosphatase");
        final DiscreteNumericalVariable bunVar =
                (DiscreteNumericalVariable)thoracicVars.get("BUN");
        // Construct this list in order by variable display name.
        final List<Value> orderedValues = Arrays.asList(
                new NumericalValue((NumericalVariable)thoracicVars.get("Age"), 66),
                DiscreteNumericalValue.fromCategory(apVar, apVar.getContainingCategory(5.0f)),
                new MultiSelectValue(asaVar, asaVar.getOptions().get(0)),
                new NumericalValue((NumericalVariable)thoracicVars.get("BMI"), 17.3f),
                DiscreteNumericalValue.fromCategory(bunVar, bunVar.getContainingCategory(10.0f)),
                new BooleanValue((BooleanVariable)thoracicVars.get("DNR"), true),
                new BooleanValue((BooleanVariable)thoracicVars.get("Preop Pneumonia"), false),
                new ProcedureValue(procedureVar, procedureVar.getProcedures().get(1)));
        
        // Create a new shuffled list to test Calculation's sorting.
        final List<Value> shuffledValues = new ArrayList<>(orderedValues);
        Collections.shuffle(shuffledValues);
        
        // Behavior verification
        fCalculationService.runCalculation(selCalc.getCalculation(), orderedValues);
        assertEquals(orderedValues, new ArrayList<>(calc.getValues()));
    }
}

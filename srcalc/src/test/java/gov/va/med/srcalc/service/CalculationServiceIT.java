package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.SampleSpecialties;
import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.domain.workflow.NewCalculation;
import gov.va.med.srcalc.domain.workflow.SelectedCalculation;

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
public class CalculationServiceIT
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
        assertEquals(SampleSpecialties.sampleSpecialtyList(), newCalc.getPossibleSpecialties());
        // TODO: other aspects of the calculation as we determine them
    }
    
    @Test
    @Transactional  // do this all in one transaction so we can roll back
    public void testSetValidSpecialty() throws InvalidIdentifierException
    {
        final int PATIENT_DFN = 1;
        final Specialty specialty = SampleSpecialties.sampleThoracicSpecialty();
        
        // Create the class under test.
        final NewCalculation newCalc = fCalculationService.startNewCalculation(PATIENT_DFN);
        final Calculation calc = newCalc.getCalculation();
        
        // Behavior verification.
        final SelectedCalculation selCalc =
                fCalculationService.setSpecialty(calc, specialty.getName());
        assertEquals(specialty, selCalc.getCalculation().getSpecialty());
        assertEquals(1, selCalc.getVariables().size());
        assertEquals("Age", selCalc.getVariables().get(0).getDisplayName());
    }
}

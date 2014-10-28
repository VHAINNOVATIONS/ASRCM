package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.workflow.NewCalculation;
import gov.va.med.srcalc.test.util.SampleSpecialties;

import org.joda.time.DateTime;
import org.junit.Test;

public class DefaultCalculationServiceTest
{
    @Test
    public final void testStartNewCalculation()
    {
        final int PATIENT_DFN = 1;
        final DateTime testStartDateTime = new DateTime();

        // Set up the mock dao.
        final SpecialtyDao specialtyDao = mock(SpecialtyDao.class);
        when(specialtyDao.getAllSpecialties()).thenReturn(SampleSpecialties.sampleSpecialtyList());
        
        // Create the class under test.
        final DefaultCalculationService s = new DefaultCalculationService(specialtyDao);
        
        // Behavior verification.
        final NewCalculation newCalc = s.startNewCalculation(PATIENT_DFN);
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
}

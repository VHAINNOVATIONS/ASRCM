package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.domain.workflow.NewCalculation;
import gov.va.med.srcalc.domain.workflow.SelectedCalculation;
import gov.va.med.srcalc.test.util.SampleSpecialties;

import org.joda.time.DateTime;
import org.junit.Test;

public class DefaultCalculationServiceTest
{
    protected static final int SAMPLE_PATIENT_DFN = 1;

    public SpecialtyDao mockSpecialtyDao()
    {
        final SpecialtyDao dao = mock(SpecialtyDao.class);
        when(dao.getAllSpecialties()).thenReturn(SampleSpecialties.sampleSpecialtyList());
        final Specialty specialty = SampleSpecialties.sampleThoracicSpecialty();
        when(dao.getByName(specialty.getName())).thenReturn(specialty);
        return dao;
    }
    
    public DefaultCalculationService defaultCalculationService()
    {
        return new DefaultCalculationService(mockSpecialtyDao());
    }

    @Test
    public final void testStartNewCalculation()
    {
        final DateTime testStartDateTime = new DateTime();

        // Create the class under test.
        final DefaultCalculationService s = defaultCalculationService();
        
        // Behavior verification.
        final NewCalculation newCalc = s.startNewCalculation(SAMPLE_PATIENT_DFN);
        final Calculation calc = newCalc.getCalculation();
        assertEquals(SAMPLE_PATIENT_DFN, calc.getPatient().getDfn());
        assertTrue("start date not in the past",
                // DateTime has millisecond precision, so the current time may
                // still be the same. Use "less than or equal to".
                calc.getStartDateTime().compareTo(new DateTime()) <= 0);
        assertTrue("start date not after test start",
                calc.getStartDateTime().compareTo(testStartDateTime) >= 0);
        assertEquals(SampleSpecialties.sampleSpecialtyList(), newCalc.getPossibleSpecialties());
    }
    
    @Test
    public final void testSetValidSpecialty() throws InvalidIdentifierException
    {
        final Specialty thoracicSpecialty = SampleSpecialties.sampleThoracicSpecialty();
        
        // Create the class under test.
        final DefaultCalculationService s = defaultCalculationService();
        final Calculation calc = s.startNewCalculation(SAMPLE_PATIENT_DFN).getCalculation();
        
        // Behavior verification.
        final SelectedCalculation selCalc =
                s.setSpecialty(calc, thoracicSpecialty.getName());
        assertEquals(thoracicSpecialty, calc.getSpecialty());
        assertEquals(1, selCalc.getVariables().size());
        assertEquals("Age", selCalc.getVariables().get(0).getDisplayName());
    }
    
    @Test(expected = InvalidIdentifierException.class)
    public final void testSetInvalidSpecialty() throws InvalidIdentifierException
    {
        final int PATIENT_DFN = 1;
        
        // Create the class under test.
        final DefaultCalculationService s = defaultCalculationService();
        final Calculation calc = s.startNewCalculation(PATIENT_DFN).getCalculation();
        
        // Behavior verification.
        s.setSpecialty(calc, "invalid specialty");
    }
}

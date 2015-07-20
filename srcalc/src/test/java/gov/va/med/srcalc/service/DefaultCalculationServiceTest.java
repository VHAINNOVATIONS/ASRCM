package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.vista.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link DefaultCalculationService} class. Note that these are unit tests, not
 * integration tests: tests are accomplished via mocks.
 */
public class DefaultCalculationServiceTest
{
    protected static final int SAMPLE_PATIENT_DFN = 1;
    
    private static final String VALID_ESIG_CODE = "eSigCode";
    
    private SpecialtyDao fMockSpecialtyDao;
    private VistaPatientDao fPatientDao;
    private VistaSurgeryDao fSurgeryDao;
    
    @Before
    public void setup()
    {
        // Make the SpecialtyDao actually return specialties.
        fMockSpecialtyDao = mock(SpecialtyDao.class);
        when(fMockSpecialtyDao.getAllSpecialties()).thenReturn(SampleModels.specialtyList());
        final Specialty specialty = SampleModels.thoracicSpecialty();
        when(fMockSpecialtyDao.getByName(specialty.getName())).thenReturn(specialty);
        
        // And make VistaPatientDao.getPatient actually return a patient.
        fPatientDao = mock(VistaPatientDao.class);
        when(fPatientDao.getPatient(SAMPLE_PATIENT_DFN))
            .thenReturn(SampleCalculations.dummyPatient(SAMPLE_PATIENT_DFN));
        // Default to bad signature.
        when(fPatientDao.saveRiskCalculationNote(anyInt(), anyString(), anyString()))
            .thenReturn(VistaPatientDao.SaveNoteCode.INVALID_SIGNATURE);
        when(fPatientDao.saveRiskCalculationNote(eq(SAMPLE_PATIENT_DFN), eq(VALID_ESIG_CODE), anyString()))
            .thenReturn(VistaPatientDao.SaveNoteCode.SUCCESS);

        // These don't need any special setup: we just verify certain calls.
        fSurgeryDao = MockVistaDaos.mockSurgeryDao();
    }
    
    @Test
    public final void testGetValidSpecialties()
    {
        // Create the class under test.
        final DefaultCalculationService s = new DefaultCalculationService(
                fMockSpecialtyDao, fPatientDao, fSurgeryDao);

        // Behavior verification.
        assertEquals(SampleModels.specialtyList(), s.getValidSpecialties());
    }

    @Test
    public final void testStartNewCalculation()
    {
        final DateTime testStartDateTime = new DateTime();

        // Create the class under test.
        final DefaultCalculationService s = new DefaultCalculationService(
                fMockSpecialtyDao, fPatientDao, fSurgeryDao);
        
        // Behavior verification.
        final Calculation calc = s.startNewCalculation(SAMPLE_PATIENT_DFN);
        assertEquals(SAMPLE_PATIENT_DFN, calc.getPatient().getDfn());
        assertTrue("start date not in the past",
                // DateTime has millisecond precision, so the current time may
                // still be the same. Use "less than or equal to".
                calc.getStartDateTime().compareTo(new DateTime()) <= 0);
        assertTrue("start date not after test start",
                calc.getStartDateTime().compareTo(testStartDateTime) >= 0);
    }
    
    @Test
    public final void testSetValidSpecialty() throws InvalidIdentifierException
    {
        final Specialty thoracicSpecialty = SampleModels.thoracicSpecialty();
        
        // Create the class under test.
        final DefaultCalculationService s = new DefaultCalculationService(
                fMockSpecialtyDao, fPatientDao, fSurgeryDao);
        final Calculation calc = s.startNewCalculation(SAMPLE_PATIENT_DFN);
        
        // Behavior verification.
        s.setSpecialty(calc, thoracicSpecialty.getName());
        assertEquals(thoracicSpecialty, calc.getSpecialty());
    }
    
    @Test(expected = InvalidIdentifierException.class)
    public final void testSetInvalidSpecialty() throws InvalidIdentifierException
    {
        final int PATIENT_DFN = 1;
        
        // Create the class under test.
        final DefaultCalculationService s = new DefaultCalculationService(
                fMockSpecialtyDao, fPatientDao, fSurgeryDao);
        final Calculation calc = s.startNewCalculation(PATIENT_DFN);
        
        // Behavior verification.
        s.setSpecialty(calc, "invalid specialty");
    }
    
    @Test
    public final void testSignCalculation() throws Exception
    {
        final DefaultCalculationService s = new DefaultCalculationService(
                fMockSpecialtyDao, fPatientDao, fSurgeryDao);
        final CalculationResult result = SampleCalculations.thoracicResult();
        final SignedResult expectedSignedResult = result.signed();

        // Behavior
        s.signRiskCalculation(result, VALID_ESIG_CODE);
        
        // Verification
        verify(fPatientDao).saveRiskCalculationNote(
                result.getPatientDfn(), VALID_ESIG_CODE, result.buildNoteBody());
        // SignedResult.equals() compares the times at second precision, so
        // this test will fail if the second happens to roll over during this
        // test. The chance of this happening should be <1%.
        verify(fSurgeryDao).saveCalculationResult(expectedSignedResult);
    }
    
    @Test
    public final void testSignCalculationInvalidSig() throws Exception
    {
        final String invalidSigCode = "invalidCode";
        final DefaultCalculationService s = new DefaultCalculationService(
                fMockSpecialtyDao, fPatientDao, fSurgeryDao);
        final CalculationResult result = SampleCalculations.thoracicResult();

        // Behavior
        s.signRiskCalculation(result, invalidSigCode);
        
        // Verification
        verify(fPatientDao).saveRiskCalculationNote(
                result.getPatientDfn(), invalidSigCode, result.buildNoteBody());
        verify(fSurgeryDao, never()).saveCalculationResult(any(SignedResult.class));
    }
}

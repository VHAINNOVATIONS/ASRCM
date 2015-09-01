package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import gov.va.med.srcalc.db.ResultsDao;
import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.test.util.TestAuthnProvider;
import gov.va.med.srcalc.vista.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

/**
 * Tests the {@link DefaultCalculationService} class. Note that these are unit tests, not
 * integration tests: tests are accomplished via mocks.
 */
public class DefaultCalculationServiceTest
{
    protected static final int SAMPLE_PATIENT_DFN = 1;
    
    private static final String VALID_ESIG_CODE = "eSigCode";
    
    private SpecialtyDao fMockSpecialtyDao;
    private VistaPatientDao fMockPatientDao;
    private VistaSurgeryDao fMockSurgeryDao;
    private ResultsDao fMockResultsDao;
    
    @Rule
    public final TestAuthnProvider fAuthnProvider = new TestAuthnProvider();
    
    @Before
    public void setup()
    {
        final MockVistaDaoFactory mockVistaDaos = new MockVistaDaoFactory();
        
        // Make the SpecialtyDao actually return specialties.
        fMockSpecialtyDao = mock(SpecialtyDao.class);
        when(fMockSpecialtyDao.getAllSpecialties()).thenReturn(SampleModels.specialtyList());
        final Specialty specialty = SampleModels.thoracicSpecialty();
        when(fMockSpecialtyDao.getByName(specialty.getName())).thenReturn(specialty);
        
        // And make VistaPatientDao.getPatient actually return a patient.
        fMockPatientDao = mockVistaDaos.getVistaPatientDao();
        when(fMockPatientDao.getPatient(SAMPLE_PATIENT_DFN))
            .thenReturn(SampleCalculations.dummyPatient(SAMPLE_PATIENT_DFN));
        // Default to bad signature.
        when(fMockPatientDao.saveRiskCalculationNote(anyInt(), anyString(), anyString()))
            .thenReturn(VistaPatientDao.SaveNoteCode.INVALID_SIGNATURE);
        when(fMockPatientDao.saveRiskCalculationNote(
                eq(SAMPLE_PATIENT_DFN), eq(VALID_ESIG_CODE), anyString()))
            .thenReturn(VistaPatientDao.SaveNoteCode.SUCCESS);

        // These don't need any special setup: we just verify certain calls.
        fMockSurgeryDao = mockVistaDaos.getVistaSurgeryDao();
        fMockResultsDao = mock(ResultsDao.class);
    }
    
    /**
     * Constructs a new DefaultCalculationService using the above mock DAOs.
     */
    private DefaultCalculationService createWithMocks()
    {
        return new DefaultCalculationService(
                fMockSpecialtyDao, fMockPatientDao, fMockSurgeryDao, fMockResultsDao);
    }
    
    @Test
    public final void testGetValidSpecialties()
    {
        // Create the class under test.
        final DefaultCalculationService s = createWithMocks();

        // Behavior verification.
        assertEquals(SampleModels.specialtyList(), s.getValidSpecialties());
    }

    @Test
    public final void testStartNewCalculation()
    {
        final DateTime testStartDateTime = DateTime.now();

        // Create the class under test.
        final DefaultCalculationService s = createWithMocks();
        
        // Behavior verification.
        final Calculation calc = s.startNewCalculation(SAMPLE_PATIENT_DFN);
        assertEquals(SAMPLE_PATIENT_DFN, calc.getPatient().getDfn());
        assertTrue("start date should be in the past",
                // DateTime has millisecond precision, so the current time may
                // still be the same. Use "less than or equal to".
                calc.getStartDateTime().compareTo(DateTime.now()) <= 0);
        assertTrue("start date should be after test start",
                calc.getStartDateTime().compareTo(testStartDateTime) >= 0);
    }
    
    @Test
    public final void testSetValidSpecialty() throws InvalidIdentifierException
    {
        final Specialty thoracicSpecialty = SampleModels.thoracicSpecialty();
        
        // Create the class under test.
        final DefaultCalculationService s = createWithMocks();
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
        final DefaultCalculationService s = createWithMocks();
        final Calculation calc = s.startNewCalculation(PATIENT_DFN);
        
        // Behavior verification.
        s.setSpecialty(calc, "invalid specialty");
    }
    
    @Test
    public final void testRunCalculation() throws Exception
    {
        // Setup
        final Specialty thoracicSpecialty = SampleModels.thoracicSpecialty();
        final DefaultCalculationService s = createWithMocks();
        final Calculation calc = s.startNewCalculation(SAMPLE_PATIENT_DFN);
        s.setSpecialty(calc, thoracicSpecialty.getName());
        final ImmutableMap<AbstractVariable, Value> thoracicValues =
                SampleCalculations.thoracicValues();
        
        /* Behavior & Verification */
        
        s.runCalculation(calc, thoracicValues.values());
        // First run: must persist the HistoricalCalculation.
        verify(fMockResultsDao, times(1))
            .persistHistoricalCalc(calc.getHistoricalCalculation().get());
        
        // Try now with tweaked values and verify that the service didn't try to
        // re-persist the HistoricalCalculation.
        final HashMap<AbstractVariable, Value> tweakedValues =
                new HashMap<>(thoracicValues);
        final ProcedureVariable procVar = SampleModels.procedureVariable();
        tweakedValues.put(procVar, procVar.makeValue(SampleModels.repairRightProcedure()));
        s.runCalculation(calc, tweakedValues.values());
        // Verify that persistHistoricalCalc() wasn't called any more times.
        verify(fMockResultsDao, times(1))
            .persistHistoricalCalc(any(HistoricalCalculation.class));
    }
    
    @Test
    public final void testSignCalculation() throws Exception
    {
        final DefaultCalculationService s = createWithMocks();
        final CalculationResult result = SampleCalculations.thoracicResult();
        final SignedResult expectedSignedResult = result.signed();

        // Behavior
        s.signRiskCalculation(result, VALID_ESIG_CODE);
        
        // Verification
        verify(fMockPatientDao).saveRiskCalculationNote(
                result.getPatientDfn(), VALID_ESIG_CODE, result.buildNoteBody());
        // SignedResult.equals() compares the times at second precision, so this test will
        // fail if the second happens to roll over during this test. The chance of this
        // happening should be <1%.
        verify(fMockSurgeryDao).saveCalculationResult(expectedSignedResult);
        verify(fMockResultsDao).persistSignedResult(expectedSignedResult);
    }
    
    @Test
    public final void testSignCalculationInvalidSig() throws Exception
    {
        final String invalidSigCode = "invalidCode";
        final DefaultCalculationService s = createWithMocks();
        final CalculationResult result = SampleCalculations.thoracicResult();

        // Behavior
        s.signRiskCalculation(result, invalidSigCode);
        
        // Verification
        verify(fMockPatientDao).saveRiskCalculationNote(
                result.getPatientDfn(), invalidSigCode, result.buildNoteBody());
        verify(fMockSurgeryDao, never()).saveCalculationResult((SignedResult)anyObject());
        verify(fMockResultsDao, never()).persistSignedResult((SignedResult)anyObject());
    }
}

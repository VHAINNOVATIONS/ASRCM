package gov.va.med.srcalc.vista;

import static org.mockito.Mockito.*;
import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.Procedure;
import gov.va.med.srcalc.domain.model.SampleModels;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

import com.google.common.base.Optional;
import com.google.common.collect.*;

public class RpcVistaSurgeryDaoTest
{
    private final static String RADIOLOGIST_DUZ = "11716";
    private final static int PATIENT_DFN = 500;
    
    private VistaProcedureCaller fProcedureCaller;
    
    @Before
    public void setup()
    {
        fProcedureCaller = mock(VistaProcedureCaller.class);
        // Default: return an error regarding Patient DFN.
        when(fProcedureCaller.doSaveRiskCalculationCall(
                    anyString(),
                    anyString(),
                    anyString(),
                    anyString(),
                    anyListOf(String.class)))
                .thenReturn("0^Patient DFN is not valid");
        // If the expected DFN is provided, return success.
        when(fProcedureCaller.doSaveRiskCalculationCall(
                    anyString(),
                    eq(String.valueOf(PATIENT_DFN)),
                    anyString(),
                    anyString(),
                    anyListOf(String.class)))
                .thenReturn(RemoteProcedure.RISK_SAVED_RETURN);
    }

    @Test
    public final void testSaveCalculationResultWithCpt()
    {
        // Create a test calculation.
        final ImmutableMap<String, Float> outcomes = ImmutableMap.of(
                "Thoracic 30-day", .301f,
                // test the 0-padding too
                "Thoracic 60-day", .032f);
        final Procedure procedure = SampleModels.repairLeftProcedure();
        // Calculate some arbitrary, but known, timestamps.
        final DateTime startDateTime = new DateTime().withDate(2015, 2, 4).withTime(10, 45, 0, 0);
        final DateTime signatureDateTime = startDateTime.withTime(13, 2, 0, 0);
        final SignedResult result = new SignedResult(
                PATIENT_DFN,
                "Specialty Name",
                Optional.of(procedure.getCptCode()),
                startDateTime,
                signatureDateTime,
                outcomes);
        
        // Expected data
        final ImmutableList<String> outcomesStrings = ImmutableList.of(
                "Thoracic 30-day^30.1", "Thoracic 60-day^03.2");

        final RpcVistaSurgeryDao dao = new RpcVistaSurgeryDao(
                fProcedureCaller, RADIOLOGIST_DUZ);

        // Behavior
        dao.saveCalculationResult(result);
        
        // Verification. saveCalculationResult() doesn't return anything, so
        // verify that the correct RPC parameters were given instead.
        verify(fProcedureCaller).doSaveRiskCalculationCall(
                RADIOLOGIST_DUZ,
                String.valueOf(PATIENT_DFN),
                procedure.getCptCode(),
                "02/04/2015@1302",
                outcomesStrings);
    }
    
    @Test
    public final void testSaveCalculationResultWithoutCpt()
    {
        // Create a test calculation.
        final ImmutableMap<String, Float> outcomes = ImmutableMap.of(
                "Cardiac 30-day", .900f,
                // test the 0-padding too
                "Cardiac 60-day", .092f);
        // Calculate some arbitrary, but known, timestamps.
        final DateTime startDateTime = new DateTime().withDate(2014, 3, 9).withTime(9, 17, 0, 0);
        final DateTime signatureDateTime = startDateTime.withTime(17, 10, 0, 0);
        final SignedResult result = new SignedResult(
                PATIENT_DFN,
                "Cardiac CABG",
                Optional.<String>absent(),
                startDateTime,
                signatureDateTime,
                outcomes);
        
        // Expected data
        final ImmutableList<String> outcomesStrings = ImmutableList.of(
                "Cardiac 30-day^90.0", "Cardiac 60-day^09.2");
        
        final RpcVistaSurgeryDao dao = new RpcVistaSurgeryDao(
                fProcedureCaller, RADIOLOGIST_DUZ);
        
        // Behavior
        dao.saveCalculationResult(result);
        
        // Verification. saveCalculationResult() doesn't return anything, so
        // verify that the correct RPC parameters were given instead.
        verify(fProcedureCaller).doSaveRiskCalculationCall(
                RADIOLOGIST_DUZ,
                String.valueOf(PATIENT_DFN),
                "",
                "03/09/2014@1710",
                outcomesStrings);
    }
    
    @Test(expected = DataAccessException.class)
    public final void testSaveInvalidPatient()
    {
        final SignedResult result = new SignedResult(
                99,
                "Cardiac CABG",
                Optional.<String>absent(),
                new DateTime(),
                new DateTime(),
                ImmutableMap.<String, Float>of());
        
        final RpcVistaSurgeryDao dao = new RpcVistaSurgeryDao(
                fProcedureCaller, RADIOLOGIST_DUZ);
        
        // Behavior
        dao.saveCalculationResult(result);
    }
    
}

package gov.va.med.srcalc.vista;

import static org.mockito.Mockito.*;

import javax.security.auth.login.LoginException;

import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.Procedure;
import gov.va.med.srcalc.domain.model.SampleModels;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

import com.google.common.base.Optional;
import com.google.common.collect.*;

/**
 * Tests the {@link RpcVistaSurgeryDao} class.
 */
public class RpcVistaSurgeryDaoTest
{
    private final static String RADIOLOGIST_DUZ = "11716";
    private final static int PATIENT_DFN = 500;
    
    private VistaProcedureCaller fProcedureCaller;
    
    /**
     * Creates a {@link SignedResult} for testing, with dummy values for all of the
     * fields we don't save to VistA.
     */
    private SignedResult makeResult(
            final int patientDfn,
            final Optional<String> cptCode,
            final DateTime signatureDateTime,
            final ImmutableMap<String, Float> outcomes)
    {
        // We don't actually save the startTimestamp to VistA, but at least generate a
        // realistic one.
        final DateTime startDateTime = signatureDateTime.minusMinutes(40);
        final HistoricalCalculation historicalCalc = new HistoricalCalculation(
                "Specialty Name",
                "442",
                startDateTime,
                3,
                Optional.<String>absent());
        return new SignedResult(
                historicalCalc,
                patientDfn,
                cptCode,
                signatureDateTime,
                ImmutableMap.<String, String>of(),
                outcomes);
    }
    
    @Before
    public void setup()
    {
        fProcedureCaller = mock(VistaProcedureCaller.class);
        try
        {
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
        catch (final LoginException ex)
        {
            // The compiler sees a possible LoginException, but it is just an artifact
            // of Mockito's mocking API.
            throw new RuntimeException("Unexpected Exception.", ex);
        }
    }

    @Test
    public final void testSaveCalculationResultWithCpt() throws Exception
    {
        // Create a test calculation.
        final ImmutableMap<String, Float> outcomes = ImmutableMap.of(
                "Thoracic 30-day", .301f,
                // test the 0-padding too
                "Thoracic 60-day", .032f);
        final Procedure procedure = SampleModels.repairLeftProcedure();
        // Calculate some arbitrary, but known, timestamps.
        final DateTime signatureDateTime = new DateTime(2015, 2, 4, 13, 2);
        final SignedResult result = makeResult(
                PATIENT_DFN,
                Optional.of(procedure.getCptCode()),
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
    public final void testSaveCalculationResultWithoutCpt() throws Exception
    {
        // Create a test calculation.
        final ImmutableMap<String, Float> outcomes = ImmutableMap.of(
                "Cardiac 30-day", .900f,
                // test the 0-padding too
                "Cardiac 60-day", .092f);
        // Calculate some arbitrary, but known, timestamps.
        final DateTime signatureDateTime = new DateTime(2014, 3, 9, 17, 10);
        final SignedResult result = makeResult(
                PATIENT_DFN, Optional.<String>absent(), signatureDateTime, outcomes);
        
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
        final SignedResult result = makeResult(
                99,
                Optional.<String>absent(),
                DateTime.now(),
                ImmutableMap.<String, Float>of());
        
        final RpcVistaSurgeryDao dao = new RpcVistaSurgeryDao(
                fProcedureCaller, RADIOLOGIST_DUZ);
        
        // Behavior
        dao.saveCalculationResult(result);
    }
    
}

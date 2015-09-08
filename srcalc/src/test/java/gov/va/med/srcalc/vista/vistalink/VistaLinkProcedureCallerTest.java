package gov.va.med.srcalc.vista.vistalink;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.LoginException;

import gov.va.med.srcalc.domain.VistaLabs;
import gov.va.med.srcalc.vista.RemoteProcedure;
import gov.va.med.srcalc.vista.vistalink.VistaLinkProcedureCaller;
import gov.va.med.vistalink.adapter.cci.VistaLinkDuzConnectionSpec;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link VistaLinkProcedureCaller} class.
 */
public class VistaLinkProcedureCallerTest
{
    @Before
    public void setUp() throws Exception
    {
        VistaLinkUtilTest.setupJndiForVistaLink();
    }
    
    /**
     * Verifies execution of {@link RemoteProcedure#XUS_GET_USER_INFO}, which requires a
     * different RPC context.
     */
    @Test
    public final void testDoUserRpc() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(VistaLinkUtilTest.SUPPORTED_DIVISON);
        
        final List<String> results = caller.doRpc(
                MockVistaLinkConnection.RADIOLOGIST_DUZ, RemoteProcedure.XUS_GET_USER_INFO);
        // Just verify the name.
        assertEquals(MockVistaLinkConnection.RADIOLOGIST_NAME, results.get(1));
    }
    
    @Test
    public final void testDoPatientRpc() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(VistaLinkUtilTest.SUPPORTED_DIVISON);
        final List<String> results = caller.doRpc(
                MockVistaLinkConnection.RADIOLOGIST_DUZ,
                RemoteProcedure.SR_ASRC_GET_PATIENT,
                MockVistaLinkConnection.PATIENT_DFN);
        assertEquals(
                Arrays.asList(MockVistaLinkConnection.PATIENT_DATA),
                results);
    }
    
    @Test
    public final void testDoSaveProgressNoteCall() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(VistaLinkUtilTest.SUPPORTED_DIVISON);
        final String result = caller.doSaveProgressNoteCall(
                MockVistaLinkConnection.RADIOLOGIST_DUZ,
                "fakeEncryptedSig",
                MockVistaLinkConnection.PATIENT_DFN,
                Arrays.asList("line1", "line2"));
        
        assertEquals(RemoteProcedure.VALID_SIGNATURE_RETURN, result);
    }
    
    @Test
    public final void testDoSaveRiskCalculationCall() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(VistaLinkUtilTest.SUPPORTED_DIVISON);
        final String result = caller.doSaveRiskCalculationCall(
                MockVistaLinkConnection.RADIOLOGIST_DUZ,
                MockVistaLinkConnection.PATIENT_DFN,
                "12345",
                "01/01/2015@1001",
                Arrays.asList("Model^02.1"));
        
        assertEquals(RemoteProcedure.RISK_SAVED_RETURN, result);
    }
    
    @Test
    public final void testDoRetrieveLabsCallSgot() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(VistaLinkUtilTest.SUPPORTED_DIVISON);
        final String result = caller.doRetrieveLabsCall(
                MockVistaLinkConnection.RADIOLOGIST_DUZ,
                MockVistaLinkConnection.PATIENT_DFN,
                VistaLabs.SGOT.getPossibleLabNames());
        
        assertEquals(MockVistaLinkConnection.SGOT_LAB_DATA, result);
    }
    
    @Test
    public final void testDoRetrieveLabsCallAlbumin() throws Exception
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(VistaLinkUtilTest.SUPPORTED_DIVISON);
        final String result = caller.doRetrieveLabsCall(
                MockVistaLinkConnection.RADIOLOGIST_DUZ,
                MockVistaLinkConnection.PATIENT_DFN,
                VistaLabs.ALBUMIN.getPossibleLabNames());
        
        assertEquals(MockVistaLinkConnection.ALBUMIN_LAB_DATA, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testInvalidDivision()
    {
        final String division = "600";
        
        new VistaLinkProcedureCaller(division);
    }
    
    /**
     * Tests that {@link
     * VistaLinkProcedureCaller#doRpc(gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec,
     * RemoteProcedure, String...)} throws an AccountNotFoundException for an unknown DUZ,
     * per specification.
     */
    @Test(expected = AccountNotFoundException.class)
    public final void testUnknownDuzConnectionSpec() throws LoginException
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(VistaLinkUtilTest.SUPPORTED_DIVISON);
        
        final VistaLinkDuzConnectionSpec cs = new VistaLinkDuzConnectionSpec(
                VistaLinkUtilTest.SUPPORTED_DIVISON, "11111");
        caller.doRpc(cs, RemoteProcedure.XUS_GET_USER_INFO);
    }
    
    /**
     * Tests that {@link VistaLinkProcedureCaller#doRpc(String, RemoteProcedure, String...)}
     * throws a LoginException for an unknown DUZ, per specification.
     */
    @Test(expected = LoginException.class)
    public final void testUnknownDuz() throws LoginException
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(VistaLinkUtilTest.SUPPORTED_DIVISON);
        caller.doRpc("12222", RemoteProcedure.SR_ASRC_PERSON_CLASSES);
    }
    
    @Test(expected = LoginException.class)
    public final void testDisabledDuz() throws LoginException
    {
        final VistaLinkProcedureCaller caller =
                new VistaLinkProcedureCaller(VistaLinkUtilTest.SUPPORTED_DIVISON);
        
        caller.doRpc(MockVistaLinkConnection.DISABLED_DUZ, RemoteProcedure.XUS_GET_USER_INFO);
    }
    
}

package gov.va.med.srcalc.vista;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.va.med.srcalc.domain.Patient;

import java.util.Arrays;

import org.junit.Test;

public class RpcVistaPatientDaoTest
{
	private final static String RADIOLOGIST_DUZ = "11716";
    private final static String RPC_RETURN = "TESTPATIENT^50^M";

    @Test
    public final void testLoadVistaPatientValid()
    {
        // Setup
        final int dfn = 500;
        final VistaProcedureCaller caller = mock(VistaProcedureCaller.class);
        when(caller.doRpc(RADIOLOGIST_DUZ, RemoteProcedure.GET_PATIENT, String.valueOf(dfn)))
            .thenReturn(Arrays.asList(RPC_RETURN));
        final RpcVistaPatientDao dao = new RpcVistaPatientDao(caller, RADIOLOGIST_DUZ);

        // Behavior verification
        final Patient patient = dao.getPatient(dfn);
        assertEquals(dfn, patient.getDfn());
        assertEquals("TESTPATIENT", patient.getName());
        assertEquals("Male", patient.getGender());
        assertEquals(50, patient.getAge());
    }
    
}

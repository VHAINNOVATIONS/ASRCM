package gov.va.med.srcalc.vista;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import gov.va.med.srcalc.domain.VistaPerson;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

/**
 * Tests the {@link RpcVistaPersonDao} class.
 */
public class RpcVistaPersonDaoTest
{
    private final static String DIVISION = "512";
    private final static String RADIOLOGIST_DUZ = "11716";
    private final static String RADIOLOGIST_NAME = "RADIOLOGIST,ONE";
    private final static String RADIOLOGIST_PROVIDER_TYPE =
                    "Podiatric Medicine and Surgery Service Providers";
    
    private VistaProcedureCaller mockProcedureCaller()
    {
        final VistaProcedureCaller caller = mock(VistaProcedureCaller.class);
        when(caller.getDivision()).thenReturn(DIVISION);
        when(caller.doRpc(RADIOLOGIST_DUZ, RemoteProcedure.GET_USER))
            .thenReturn(Arrays.asList(RADIOLOGIST_NAME));
        // Test returning an extra type.
        when(caller.doRpc(RADIOLOGIST_DUZ, RemoteProcedure.GET_USER_PERSON_CLASSES))
            .thenReturn(ImmutableList.of(RADIOLOGIST_PROVIDER_TYPE, "Extra Type"));
        return caller;
    }

    @Test
    public final void testLoadVistaPersonValid()
    {
        // Setup
        final RpcVistaPersonDao dao = new RpcVistaPersonDao(mockProcedureCaller());

        // Behavior verification
        final VistaPerson person = dao.loadVistaPerson(RADIOLOGIST_DUZ);
        assertEquals(RADIOLOGIST_DUZ, person.getDuz());
        assertEquals(RADIOLOGIST_NAME, person.getDisplayName());
        assertEquals(DIVISION, person.getStationNumber());
        assertEquals(Optional.of(RADIOLOGIST_PROVIDER_TYPE), person.getProviderType());
    }
    
}

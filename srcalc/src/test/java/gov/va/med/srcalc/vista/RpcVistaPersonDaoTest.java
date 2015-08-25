package gov.va.med.srcalc.vista;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.security.auth.login.AccountException;

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
        final ImmutableList<String> userInfoMockReturn = ImmutableList.of(
                RADIOLOGIST_DUZ,
                RADIOLOGIST_NAME,
                // We don't use these values right now, but provide realistic ones anyway.
                "One Radiologist MD",
                "100^CAMP MASTER^512",
                "PHYSICIAN",
                "MEDICAL");
        try
        {
            when(caller.getDivision()).thenReturn(DIVISION);
            when(caller.doRpc(RADIOLOGIST_DUZ, RemoteProcedure.GET_USER_INFO))
                .thenReturn(userInfoMockReturn);
            // Test returning an extra type.
            when(caller.doRpc(RADIOLOGIST_DUZ, RemoteProcedure.GET_USER_PERSON_CLASSES))
                .thenReturn(ImmutableList.of(RADIOLOGIST_PROVIDER_TYPE, "Extra Type"));
            return caller;
        }
        catch (final AccountException ex)
        {
            // The compiler sees a possible AccountException, but it is just an artifact
            // of Mockito's mocking API.
            throw new RuntimeException("Unexpected Exception.", ex);
        }
    }

    @Test
    public final void testLoadVistaPersonValid() throws Exception
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

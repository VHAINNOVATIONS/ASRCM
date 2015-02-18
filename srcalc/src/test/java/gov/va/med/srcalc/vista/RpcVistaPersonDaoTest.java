package gov.va.med.srcalc.vista;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import gov.va.med.srcalc.domain.VistaPerson;

import org.junit.Test;

public class RpcVistaPersonDaoTest
{
    private final static String RADIOLOGIST_DUZ = "11716";
    private final static String RADIOLOGIST_NAME = "RADIOLOGIST,ONE";

    @Test
    public final void testLoadVistaPersonValid()
    {
        // Setup
        final String division = "500";
        final VistaProcedureCaller caller = mock(VistaProcedureCaller.class);
        when(caller.getDivision()).thenReturn(division);
        when(caller.doRpc(RADIOLOGIST_DUZ, "SR ASRC USER")).thenReturn(
                Arrays.asList(RADIOLOGIST_NAME));
        final RpcVistaPersonDao dao = new RpcVistaPersonDao(caller);

        // Behavior verification
        final VistaPerson person = dao.loadVistaPerson(RADIOLOGIST_DUZ);
        assertEquals(RADIOLOGIST_DUZ, person.getDuz());
        assertEquals(RADIOLOGIST_NAME, person.getDisplayName());
        assertEquals(division, person.getDivision());
    }
    
}

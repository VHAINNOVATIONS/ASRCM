package gov.va.med.srcalc.vista;

import static org.junit.Assert.*;

import javax.naming.NamingException;
import javax.resource.ResourceException;

import gov.va.med.srcalc.domain.VistaPerson;

import org.junit.Before;
import org.junit.Test;

public class VistaLinkVistaPersonDaoTest
{
    private final static String RADIOLOGIST_DUZ = "11716";
    
    @Before
    public void setup() throws NamingException, ResourceException
    {
        // TODO: can we just mock the VistaLinkProcedureCaller instead?
        VistaProcedureCallerTest.populateJndiWithMockVlcf();
    }

    @Test
    public final void testLoadVistaPersonValid()
    {
        final String division = "500";
        
        final VistaLinkVistaPersonDao dao = new VistaLinkVistaPersonDao(division);
        final VistaPerson person = dao.loadVistaPerson(RADIOLOGIST_DUZ);
        assertEquals(RADIOLOGIST_DUZ, person.getDuz());
        assertEquals(MockVistaLinkConnection.RADIOLOGIST_NAME, person.getDisplayName());
    }
    
}

package gov.va.med.srcalc.vista;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.naming.NamingException;
import javax.resource.ResourceException;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.vistalink.adapter.cci.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class VistaLinkVistaDaoTest
{
    private final static String RADIOLOGIST_DUZ = "11716";
    private final static String RADIOLOGIST_NAME = "RADIOLOGIST,ONE";
    private final static String VLCF_JNDI_NAME = "java:comp/env/vlj/Asrc500";
    
    @Before
    public void setup() throws NamingException, ResourceException
    {
        // Populate JNDI with a mock VistaLinkConnectionFactory that will return
        // a MockVistaLinkConnection.
        SimpleNamingContextBuilder builder =
                SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final VistaLinkConnectionFactory vlcf = mock(VistaLinkConnectionFactory.class);
        when(vlcf.getConnection(isNotNull(VistaLinkConnectionSpec.class)))
            .thenReturn(new MockVistaLinkConnection(RADIOLOGIST_NAME));
        builder.bind(VLCF_JNDI_NAME, vlcf);
    }

    @Test
    public final void testLoadVistaPersonValid()
    {
        final String division = "500";
        
        final VistaLinkVistaDao dao = new VistaLinkVistaDao(division);
        final VistaPerson person = dao.loadVistaPerson(RADIOLOGIST_DUZ);
        assertEquals(RADIOLOGIST_DUZ, person.getDuz());
        assertEquals(RADIOLOGIST_NAME, person.getDisplayName());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testLoadVistaPersonInvalidDivision()
    {
        final String division = "600";
        
        final VistaLinkVistaDao dao = new VistaLinkVistaDao(division);
        dao.loadVistaPerson(RADIOLOGIST_DUZ);
    }
    
}

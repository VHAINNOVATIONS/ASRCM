package gov.va.med.srcalc.vista;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;
import javax.resource.ResourceException;

import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionFactory;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class VistaProcedureCallerTest
{
    private final static String VLCF_JNDI_NAME = "java:comp/env/vlj/Asrc500";
    
    /**
     * Populate JNDI with a mock VistaLinkConnectionFactory that will return a
     * MockVistaLinkConnection.
     */
    public static void populateJndiWithMockVlcf()
            throws NamingException, ResourceException
    {
        SimpleNamingContextBuilder builder =
                SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        final VistaLinkConnectionFactory vlcf = mock(VistaLinkConnectionFactory.class);
        when(vlcf.getConnection(isNotNull(VistaLinkConnectionSpec.class)))
            .thenReturn(new MockVistaLinkConnection());
        builder.bind(VLCF_JNDI_NAME, vlcf);
    }

    @Before
    public void setUp() throws Exception
    {
        populateJndiWithMockVlcf();
    }
    
    @Test
    public final void testDoRpc()
    {
        final String division = "500";
        
        final VistaProcedureCaller caller =
                new VistaProcedureCaller(division);
        final List<String> results = caller.doRpc(
                "11111", "SR ASRC PATIENT", MockVistaLinkConnection.PATIENT_DFN);
        assertEquals(
                Arrays.asList(MockVistaLinkConnection.PATIENT_DATA),
                results);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testInvalidDivision()
    {
        final String division = "600";
        
        new VistaProcedureCaller(division);
    }
    
}

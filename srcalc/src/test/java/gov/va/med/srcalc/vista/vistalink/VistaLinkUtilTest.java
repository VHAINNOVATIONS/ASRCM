package gov.va.med.srcalc.vista.vistalink;

import static org.junit.Assert.*;
import gov.va.med.vistalink.institution.InstitutionMapping;
import gov.va.med.vistalink.institution.InstitutionMappingBadStationNumberException;
import gov.va.med.vistalink.institution.InstitutionMappingFactory;

import javax.naming.NamingException;
import javax.resource.ResourceException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * Unit tests for {@link VistaLinkUtil}. Using BDD-style tests.
 */
public class VistaLinkUtilTest
{
    /**
     * The division which {@link setupJndiForVistaLink} populates.
     */
    static final String SUPPORTED_DIVISON = "500";

    private final static String VLCF_JNDI_NAME = "java:comp/env/vlj/Asrc500";

    /**
     * Populate JNDI with a {@link MockVistaLinkConnectionFactory} and configures
     * VistALink accordingly.
     */
    static void setupJndiForVistaLink()
            throws NamingException, ResourceException, InstitutionMappingBadStationNumberException
    {
        SimpleNamingContextBuilder builder =
                SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind(VLCF_JNDI_NAME, new MockVistaLinkConnectionFactory());
        // Simulate VistALink self-configuration.
        final InstitutionMapping mapping =
                InstitutionMappingFactory.getInstitutionMapping();
        mapping.loadMappingsForJndiName(
                VLCF_JNDI_NAME, new String[] {SUPPORTED_DIVISON}, 100);
    }
    
    @Before
    public void setUp() throws Exception
    {
        setupJndiForVistaLink();
    }

    @Test
    public final void shouldReturnFalseForUnknownDivision()
    {
        assertEquals(false, VistaLinkUtil.isDivisionKnown("222"));
    }
    
    @Test
    public final void shouldReturnTrueForKnownDivision()
    {
        assertEquals(true, VistaLinkUtil.isDivisionKnown(SUPPORTED_DIVISON));
    }
}

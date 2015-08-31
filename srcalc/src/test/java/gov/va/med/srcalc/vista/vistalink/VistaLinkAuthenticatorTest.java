package gov.va.med.srcalc.vista.vistalink;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.VistaPerson;

import javax.security.auth.login.FailedLoginException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

/**
 * Unit tests for {@link VistaLinkAuthenticator}. We use a real {@link
 * VistaLinkProcedureCaller} since it is final and cannot be mocked, so these tests do not
 * test VistaLinkAuthenticator in pure isolation.
 */
public class VistaLinkAuthenticatorTest
{
    
    @Before
    public final void setUp() throws Exception
    {
        // VistaLinkProcedureCaller needs this.
        VistaLinkUtilTest.setupJndiForVistaLink();
    }
    
    @Test
    public final void testAuthenticateViaAccessVerify() throws Exception
    {
        final VistaLinkAuthenticator authenticator = new VistaLinkAuthenticator(
                VistaLinkUtilTest.SUPPORTED_DIVISON);
        
        final VistaPerson actualVistaPerson = authenticator.authenticateViaAccessVerify(
                MockVistaLinkConnection.RADIOLOGIST_ACCESS_CODE,
                MockVistaLinkConnection.RADIOLOGIST_VERIFY_CODE,
                "192.168.1.4");
        
        // VistaPerson doesn't provide value equality, so test significant attributes.
        assertEquals(
                MockVistaLinkConnection.RADIOLOGIST_NAME, actualVistaPerson.getDisplayName());
        assertEquals(
                Optional.of(MockVistaLinkConnection.RADIOLOGIST_PROVIDER_TYPE),
                actualVistaPerson.getProviderType());
    }
    
    @Test(expected = FailedLoginException.class)
    public final void testAuthenticateViaAccessVerifyInvalid() throws Exception
    {
        final VistaLinkAuthenticator authenticator = new VistaLinkAuthenticator(
                VistaLinkUtilTest.SUPPORTED_DIVISON);
        
        authenticator.authenticateViaAccessVerify("bob", "robert", "192.168.1.4");
    }
    
}

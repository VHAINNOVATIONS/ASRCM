package gov.va.med.srcalc.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

import javax.security.auth.login.FailedLoginException;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.vista.MockVistaDaoFactory;
import gov.va.med.srcalc.vista.VistaAuthenticator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Unit tests for {@link CcowTokenAuthenticationProvider}. Using BDD-style tests.
 */
public class CcowTokenAuthenticationProviderTest
{
    private MockVistaDaoFactory fVistaDaoFactory;
    
    @Before
    public final void setup()
    {
        // Construct a new instance for every test to reset the mocks.
        fVistaDaoFactory = new MockVistaDaoFactory();
    }

    @Test
    public final void shouldAuthenticateValidToken()
    {
        final VistaPerson expectedPerson = SampleCalculations.radiologistPerson();
        final CcowTokenAuthenticationProvider authProvider =
                new CcowTokenAuthenticationProvider(fVistaDaoFactory);
        final UsernamePasswordAuthenticationToken providedToken =
                new UsernamePasswordAuthenticationToken("ccowToken", "");
        providedToken.setDetails(new SrcalcWebAuthnDetails(
                "192.168.1.4", MockVistaDaoFactory.KNOWN_DIVISION));
        
        /* Behavior and Verification */
        final Authentication authenticatedToken = authProvider.authenticate(providedToken);
        final VistaUserDetails actualUserDetails =
                (VistaUserDetails)authenticatedToken.getPrincipal();
        assertEquals(expectedPerson.getDuz(), actualUserDetails.getDuz());
        assertEquals(expectedPerson.getDisplayName(), actualUserDetails.getDisplayName());
    }
    
    @Test(expected = AuthenticationException.class)
    public final void shouldThrowAuthenticationExceptionForInvalidToken()
            throws Exception
    {
        final CcowTokenAuthenticationProvider authProvider =
                new CcowTokenAuthenticationProvider(fVistaDaoFactory);
        final UsernamePasswordAuthenticationToken providedToken =
                new UsernamePasswordAuthenticationToken("ccowToken", "");
        providedToken.setDetails(new SrcalcWebAuthnDetails(
                "192.168.1.4", MockVistaDaoFactory.KNOWN_DIVISION));
        // Customize the mock.
        final VistaAuthenticator mockAuthenticator =
                fVistaDaoFactory.getAuthenticator(MockVistaDaoFactory.KNOWN_DIVISION);
        when(mockAuthenticator.authenticateViaCcowToken(anyString(), anyString()))
                .thenThrow(new FailedLoginException("bad CCOW token"));
        
        /* Behavior and Verification */
        authProvider.authenticate(providedToken);
    }
    
}

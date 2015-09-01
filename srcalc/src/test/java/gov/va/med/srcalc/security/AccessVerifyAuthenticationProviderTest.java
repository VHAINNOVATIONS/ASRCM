package gov.va.med.srcalc.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
 * Unit tests for {@link AccessVerifyAuthenticationProvider}. Using BDD-style tests.
 */
public class AccessVerifyAuthenticationProviderTest
{
    private MockVistaDaoFactory fVistaDaoFactory;
    
    @Before
    public final void setup()
    {
        // Construct a new instance for every test to reset the mocks.
        fVistaDaoFactory = new MockVistaDaoFactory();
    }
    
    @Test
    public final void shouldAuthenticateValidCodes()
    {
        /* Setup */
        final VistaPerson expectedPerson = SampleCalculations.radiologistPerson();
        final AccessVerifyAuthenticationProvider authProvider =
                new AccessVerifyAuthenticationProvider(fVistaDaoFactory);
        final UsernamePasswordAuthenticationToken providedToken =
                new UsernamePasswordAuthenticationToken("foo", "bar");
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
    public final void shouldThrowAuthenticationExceptionForInvalidCodes() throws Exception
    {
        /* Setup */
        final AccessVerifyAuthenticationProvider authProvider =
                new AccessVerifyAuthenticationProvider(fVistaDaoFactory);
        final UsernamePasswordAuthenticationToken providedToken =
                new UsernamePasswordAuthenticationToken("foo", "bar");
        providedToken.setDetails(new SrcalcWebAuthnDetails(
                "192.168.1.4", MockVistaDaoFactory.KNOWN_DIVISION));
        // Customize the mock.
        final VistaAuthenticator mockAuthenticator =
                fVistaDaoFactory.getAuthenticator(MockVistaDaoFactory.KNOWN_DIVISION);
        when(mockAuthenticator.authenticateViaAccessVerify(
                anyString(), anyString(), anyString()))
                .thenThrow(new FailedLoginException("bad access/verify code"));
        
        /* Behavior and Verification */
        authProvider.authenticate(providedToken);
    }
    
    @Test
    public final void shouldReturnNullForMissingDetails() throws Exception
    {
        /* Setup */
        final AccessVerifyAuthenticationProvider authProvider =
                new AccessVerifyAuthenticationProvider(fVistaDaoFactory);
        final UsernamePasswordAuthenticationToken providedToken =
                new UsernamePasswordAuthenticationToken("foo", "bar");
        
        /* Behavior and Verification */
        assertNull(authProvider.authenticate(providedToken));
    }
    
    @Test
    public final void shouldReturnNullForMissingDivision() throws Exception
    {
        /* Setup */
        final AccessVerifyAuthenticationProvider authProvider =
                new AccessVerifyAuthenticationProvider(fVistaDaoFactory);
        final UsernamePasswordAuthenticationToken providedToken =
                new UsernamePasswordAuthenticationToken("foo", "bar");
        providedToken.setDetails(new SrcalcWebAuthnDetails(
                "192.168.1.4", ""));
        
        /* Behavior and Verification */
        assertNull(authProvider.authenticate(providedToken));
    }
    
    @Test
    public final void shouldReturnNullForUnknownDivision() throws Exception
    {
        /* Setup */
        final AccessVerifyAuthenticationProvider authProvider =
                new AccessVerifyAuthenticationProvider(fVistaDaoFactory);
        final UsernamePasswordAuthenticationToken providedToken =
                new UsernamePasswordAuthenticationToken("foo", "bar");
        providedToken.setDetails(new SrcalcWebAuthnDetails(
                "192.168.1.4", "668"));
        
        /* Behavior and Verification */
        assertNull(authProvider.authenticate(providedToken));
    }
    
}

package gov.va.med.srcalc.security;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.vista.vistalink.VistaLinkAuthenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * <p>An AuthenticationProvider implementation that authenticates a user based on a VistA
 * access/verify code pair.</p>
 */
public final class AccessVerifyAuthenticationProvider implements AuthenticationProvider
{
    /**
     * The hardcoded VistA division (500). This is hardcoded for now because this
     * whole authentication implementation will change once we support context
     * sharing.
     */
    private static final String VISTA_DIVISON = "500";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(
            AccessVerifyAuthenticationProvider.class);
    
    /**
     * Returns the Authentication details as a WebAuthenticationDetails. The main purpose
     * of this method is to encapsulate exception handling.
     * @throws ConfigurationException if the authentication details is not an instance
     * of WebAuthenticationDetails
     */
    private WebAuthenticationDetails extractWebAuthDetails(
            final Authentication providedAuth)
    {
        try
        {
            return (WebAuthenticationDetails)providedAuth.getDetails();
        }
        catch (final ClassCastException ex)
        {
            throw new ConfigurationException(
                    "Authentication details must be an instance of WebAuthenticationDetails.",
                    ex);
        }
    }

    /**
     * <p>Authenticates the user with VistA.</p>
     * 
     * <p>Note that the returned Authentication is different from the given
     * Authentication. This is weird, but required by Spring Security.</p>
     * 
     * @param providedAuth should be an instance of {@link
     * UsernamePasswordAuthenticationToken} with the access code as the principal and the
     * verify code as the credentials.
     * @returns A fully-populated UsernamePasswordAuthenticationToken if authentication
     * succeeds, or null if the given Authentication is not an instance of
     * UsernamePasswordAuthenticationToken
     * @throws AuthenticationException if authentication fails
     */
    @Override
    public Authentication authenticate(final Authentication providedAuth)
        throws AuthenticationException
    {
        // Even though this is the only supported division for now, instantiate here to
        // pave the way for true division support.
        final VistaLinkAuthenticator vistaAuthenticator =
                new VistaLinkAuthenticator(VISTA_DIVISON);
        final String accessCode = providedAuth.getPrincipal().toString();
        final String verifyCode = providedAuth.getCredentials().toString();
        try
        {
            final WebAuthenticationDetails authDetails = extractWebAuthDetails(providedAuth);
            final VistaPerson vistaPerson = vistaAuthenticator.authenticateViaAccessVerify(
                    accessCode, verifyCode, authDetails.getRemoteAddress());
            final VistaUserDetails userDetails = new VistaUserDetails(vistaPerson);
            LOGGER.debug(
                    "Authentication successful: constructing fully-populated " +
                    "UsernamePasswordAuthenticationToken.");
            return new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
        }
        catch (final FailedLoginException ex)
        {
            // Due to Spring Security's architecture, we do not know whether the user was
            // actually trying to login with access/verify codes. So provide a generic
            // failure message.
            throw new BadCredentialsException("Bad credentials", ex);
        }
        catch (final LoginException ex)
        {
            // This exception is not completely accurate, but it's the closest concrete
            // subclass of AuthenticationException.
            throw new AuthenticationServiceException("VistA rejected the login", ex);
        }
        catch (final DataAccessException ex)
        {
            throw new AuthenticationServiceException(
                    "Unable to authenticate with VistA due to communication failure.",
                    ex);
        }
    }

    /**
     * Returns true if, and only if, objects of the given class can be treated as
     * {@link UsernamePasswordAuthenticationToken} objects.
     */
    @Override
    public boolean supports(final Class<?> authentication)
    {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
    
}

package gov.va.med.srcalc.security;

import javax.inject.Inject;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.vista.VistaAuthenticator;
import gov.va.med.srcalc.vista.VistaDaoFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * <p>An AuthenticationProvider implementation that authenticates a user based on a VistA
 * access/verify code pair.</p>
 * 
 * <p>Authentication is performed using a {@link VistaAuthenticator}, so this class is
 * essentially an Adapter (Gang-of-Four Design Pattern) from VistaAuthenticator to the
 * Spring Security AuthenticationProvider interface.</p>
 * 
 * @see VistaAuthenticator#authenticateViaAccessVerify(String, String, String)
 */
public final class AccessVerifyAuthenticationProvider extends VistaAuthenticationProvider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(
            AccessVerifyAuthenticationProvider.class);
    
    /**
     * Constructs an instance with the given dependencies.
     * @param vistaDaoFactory primarily for obtaining {@link VistaAuthenticator}s
     */
    @Inject
    public AccessVerifyAuthenticationProvider(final VistaDaoFactory vistaDaoFactory)
    {
        super(vistaDaoFactory);
    }
    
    /**
     * Authenticates the user given an access and verify code pair.
     * 
     * @param providedAuth contains the access code as the principal and the verify code
     * as the credentials
     * @throws AuthenticationException if authentication fails
     */
    @Override
    protected Authentication authenticate(
            final Authentication providedAuth, final SrcalcWebAuthnDetails authDetails)
            throws AuthenticationException
    {
        final String accessCode = providedAuth.getPrincipal().toString();
        final String verifyCode = providedAuth.getCredentials().toString();
        LOGGER.debug(
                "Will attempt authentication with VistA division {}",
                authDetails.getDivision());
        final VistaAuthenticator vistaAuthenticator =
                getVistaDaoFactory().getAuthenticator(authDetails.getDivision());
        try
        {
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
            LOGGER.debug("Failed access/verify code login.", ex);
            // Due to Spring Security's architecture, we do not know whether the user was
            // actually trying to login with access/verify codes. So provide a generic
            // failure message.
            throw new BadCredentialsException("Bad credentials", ex);
        }
        catch (final LoginException ex)
        {
            LOGGER.debug("VistA rejected the access/verify code login.", ex);
            // This exception is not completely accurate, but it's the closest concrete
            // subclass of AuthenticationException.
            throw new AuthenticationServiceException("VistA rejected the login", ex);
        }
        catch (final DataAccessException ex)
        {
            LOGGER.debug(
                    "Communication failure while attempting VistA authentication.", ex);
            throw new AuthenticationServiceException(
                    "Unable to authenticate with VistA due to communication failure.",
                    ex);
        }
    }
}

package gov.va.med.srcalc.security;

import javax.inject.Inject;
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
 * CCOW token.</p>
 * 
 * <p>Authentication is performed using a {@link VistaAuthenticator}, so this class is
 * essentially an Adapter (Gang-of-Four Design Pattern) from VistaAuthenticator to the
 * Spring Security AuthenticationProvider interface.</p>
 * 
 * @see VistaAuthenticator#authenticateViaCcowToken(String, String)
 */
public class CcowTokenAuthenticationProvider extends VistaAuthenticationProvider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(
            CcowTokenAuthenticationProvider.class);
    
    /**
     * Constructs an instance with the given dependencies.
     */
    @Inject
    public CcowTokenAuthenticationProvider(final VistaDaoFactory vistaDaoFactory)
    {
        super(vistaDaoFactory);
    }
    
    /**
     * Authenticates the user given a CCOW token.
     * 
     * @param providedAuth contains the CCOW token as the principal. The credentials are
     * ignored.
     * @throws AuthenticationException if authentication fails
     */
    @Override
    protected Authentication authenticate(
            final Authentication providedAuth, final SrcalcWebAuthnDetails authDetails)
            throws AuthenticationException
    {
        final String ccowToken = providedAuth.getPrincipal().toString();
        LOGGER.debug(
                "Will attempt authentication with VistA division {}",
                authDetails.getDivision());
        final VistaAuthenticator vistaAuthenticator =
                getVistaDaoFactory().getAuthenticator(authDetails.getDivision());
        try
        {
            final VistaPerson vistaPerson = vistaAuthenticator.authenticateViaCcowToken(
                    ccowToken, authDetails.getRemoteAddress());
            final VistaUserDetails userDetails = new VistaUserDetails(vistaPerson);
            LOGGER.debug(
                    "Authentication successful: constructing fully-populated " +
                    "UsernamePasswordAuthenticationToken.");
            return new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
        }
        catch (final LoginException ex)
        {
            LOGGER.debug("Failed CCOW token login.", ex);
            // Due to Spring Security's architecture, we do not know whether the user was
            // actually trying to login with a CCOW token. So provide a generic failure
            // message.
            throw new BadCredentialsException("Bad credentials", ex);
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

package gov.va.med.srcalc.security;

import gov.va.med.srcalc.vista.VistaDaoFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * An AuthenticationProvider that authenticates a user with VistA. Subclasses implement
 * various authentication mechanisms (e.g., Access/Verify codes, etc.).
 */
public abstract class VistaAuthenticationProvider implements AuthenticationProvider
{
    /**
     * Get the logger for the concrete class, not this abstract class.
     */
    private final Logger fLogger = LoggerFactory.getLogger(getClass());
    
    private final VistaDaoFactory fVistaDaoFactory;
    
    /**
     * Constructs an instance with the given dependencies.
     */
    public VistaAuthenticationProvider(final VistaDaoFactory vistaDaoFactory)
    {
        fVistaDaoFactory = vistaDaoFactory;
    }
    
    /**
     * Returns the VistaDaoFactory provided to the constructor.
     */
    public VistaDaoFactory getVistaDaoFactory()
    {
        return fVistaDaoFactory;
    }
    
    /**
     * <p>Authenticates the user with VistA.</p>
     * 
     * <p>For this method to perform authentication, the passed Authentication object must
     * have an instance of {@link SrcalcWebAuthnDetails} (with a known division) as the
     * details object. If not, it is not a programming error: this method will just return
     * null. (Spring Security specifies this behavior.)</p>
     * 
     * <p><em>Note:</em> Subclasses may put further constraints on the given
     * Authentication object. Consult their documentation.</p>
     * 
     * @param providedAuth contains the user's information necessary for authentication
     * @return A fully-populated UsernamePasswordAuthenticationToken if authentication
     * succeeds, or null if the above criteria are not met.
     * @throws AuthenticationException if authentication fails
     */
    @Override
    public Authentication authenticate(final Authentication providedAuth)
        throws AuthenticationException
    {
        final Object authDetails = providedAuth.getDetails();
        fLogger.trace("Trying to get valid SrcalcWebAuthnDetails from {}", authDetails);

        if (!(authDetails instanceof SrcalcWebAuthnDetails))
        {
            return notSupported(
                    "Provided authentication details were not SrcalcWebAuthnDetails");
        }

        // Get division.
        final SrcalcWebAuthnDetails srcalcDetails = (SrcalcWebAuthnDetails)authDetails;
        final String division = srcalcDetails.getDivision();
        if (division.isEmpty())
        {
            return notSupported("No division specified");
        }
        if (!fVistaDaoFactory.isDivisionKnown(division))
        {
            return notSupported("Unknown division");
        }
        
        return authenticate(providedAuth, srcalcDetails);
    }
    
    /**
     * Logs the given reason for not performing authentication and returns null.
     * @return always null
     */
    private Authentication notSupported(final String reason)
    {
        fLogger.info("{}: not performing authentication", reason);
        return null;
    }
    
    /**
     * Template method to authenticate the user given a generic Authentication and an
     * SrcalcWebAuthnDetails.
     * @param providedAuth contains the user's information necessary for authentication
     * @param authDetails an SrcalcWebAuthnDetails indicating a known division
     * @return A fully-populated UsernamePasswordAuthenticationToken if authentication
     * succeeds
     * @throws AuthenticationException if authentication fails
     */
    protected abstract Authentication authenticate(
            final Authentication providedAuth, final SrcalcWebAuthnDetails authDetails)
            throws AuthenticationException;
    
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

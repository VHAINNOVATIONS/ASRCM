package gov.va.med.srcalc.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Authentication- and authorization-related utility methods.
 */
public final class SecurityUtil
{
    private SecurityUtil()
    {
        // No construction.
    }

    /**
     * Returns the currently-authenticated security principal (in other words, the current
     * user).
     * @throws IllegalStateException if there is no currently-authenticated principal
     */
    public static VistaUserDetails getCurrentPrincipal()
    {
        final Authentication authn = SecurityContextHolder.getContext().getAuthentication();
        if (authn == null)
        {
            throw new IllegalStateException("No current security principal");
        }
        return (VistaUserDetails)authn.getPrincipal();
    }
}

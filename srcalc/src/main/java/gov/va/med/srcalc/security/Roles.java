package gov.va.med.srcalc.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * User roles for the application.
 */
public enum Roles
{
    ROLE_USER,
    ROLE_ADMIN;
    
    /**
     * Returns a Spring {@link GrantedAuthority} representing the role.
     * {@link GrantedAuthority#getAuthority()} will return the name of the enum
     * constant exactly as declared above, so the constant names can be used
     * as role identifiers, e.g. in XML configuration.
     */
    public GrantedAuthority asGrantedAuthority()
    {
        return new SimpleGrantedAuthority(name());
    }
}

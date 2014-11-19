package gov.va.med.srcalc.security;

import gov.va.med.srcalc.domain.VistaPerson;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Adapter from a {@link VistaPerson} to a Spring-Security {@link UserDetails}
 * for authentication and authorization.
 */
public class VistaUserDetails implements UserDetails
{
    private static final long serialVersionUID = 1L;
    
    private final VistaPerson fVistaPerson;
    
    public VistaUserDetails(final VistaPerson vistaPerson)
    {
        fVistaPerson = vistaPerson;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        // For now, all users are just users.
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    
    public String getDuz()
    {
        return fVistaPerson.getDuz();
    }
    
    /**
     * The VistA access code functions as a user name, but it is also treated as
     * private data. We just return the user's DUZ instead.
     */
    @Override
    public String getUsername()
    {
        return getDuz();
    }
    
    public String getDisplayName()
    {
        return fVistaPerson.getDisplayName();
    }
    
    /**
     * This will be the VistA verify code.
     */
    @Override
    public String getPassword()
    {
        return "";
    }
    
    @Override
    public boolean isAccountNonExpired()
    {
        // Assume true.
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked()
    {
        // Assume true.
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired()
    {
        // True for now until we actually check verify codes.
        return true;
    }
    
    @Override
    public boolean isEnabled()
    {
        // Assume true.
        return true;
    }
    
}

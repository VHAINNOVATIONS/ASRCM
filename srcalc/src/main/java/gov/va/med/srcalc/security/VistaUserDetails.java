package gov.va.med.srcalc.security;

import gov.va.med.srcalc.domain.VistaPerson;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Adapter from a {@link VistaPerson} to a Spring-Security {@link UserDetails}
 * for authentication and authorization.
 */
public class VistaUserDetails implements UserDetails
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;
    
    private final VistaPerson fVistaPerson;
    
    /**
     * Constructs an instance.
     * @param vistaPerson the wrapped VistaPerson
     */
    public VistaUserDetails(final VistaPerson vistaPerson)
    {
        fVistaPerson = vistaPerson;
    }

    /**
     * This is simple: all VistA users are treated alike, so this method always
     * returns a one-element collection containing {@link Roles#ROLE_USER}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Arrays.asList(Roles.ROLE_USER.asGrantedAuthority());
    }
    
    public String getDivision()
    {
        return fVistaPerson.getDivision();
    }
    
    public String getDuz()
    {
        return fVistaPerson.getDuz();
    }
    
    /**
     * <p>The VistA access code functions as a user name, but it is also treated as
     * private data. We just return the user's DUZ instead.</p>
     * 
     * <p>The username should probably contain the division as well (e.g.,
     * 11716@500), but we'll use just the DUZ until we start testing with
     * multiple sites.</p>
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

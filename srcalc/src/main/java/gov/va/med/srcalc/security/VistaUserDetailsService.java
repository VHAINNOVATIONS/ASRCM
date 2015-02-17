package gov.va.med.srcalc.security;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.vista.VistaPersonDao;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

public class VistaUserDetailsService implements UserDetailsService
{
    /**
     * The hardcoded VistA division (500). This is hardcoded for now because this
     * whole authentication implementation will change once we support context
     * sharing.
     */
    private final String VISTA_DIVISON = "500";
    
    private static List<String> adminDuzs = Arrays.asList("1", "20001");
    
    private final VistaDaoFactory fVistaDaoFactory;
    
    @Inject
    public VistaUserDetailsService(final VistaDaoFactory vistaDaoFactory)
    {
        fVistaDaoFactory = vistaDaoFactory;
    }

    /**
     * Loads the VistaUser for the given DUZ.
     */
    @Override
    public VistaUserDetails loadUserByUsername(final String duz) throws UsernameNotFoundException
    {
        final VistaPersonDao vistaDao = fVistaDaoFactory.getVistaPersonDao(VISTA_DIVISON);
        final VistaPerson person = vistaDao.loadVistaPerson(duz);
        return new VistaUserDetails(person, getUserAuthorities(person));
    }
    
    private List<GrantedAuthority> getUserAuthorities(VistaPerson person)
    {
        // Unmaintainable solution until we settle on where to store authorities:
        // VistA or the ASRC DB.
        final GrantedAuthority user = new SimpleGrantedAuthority("ROLE_USER");
        if (adminDuzs.contains(person.getDuz()))
        {
            return Arrays.asList(user, new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        else
        {
            return Arrays.asList(user);
        }
    }
}

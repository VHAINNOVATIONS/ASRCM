package gov.va.med.srcalc.security;

import javax.inject.Inject;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.vista.VistaPersonDao;

import org.springframework.security.core.userdetails.*;

/**
 * A Spring UserDetailsService implementation that loads users from VistA.
 */
public class VistaUserDetailsService implements UserDetailsService
{
    /**
     * The hardcoded VistA division (500). This is hardcoded for now because this
     * whole authentication implementation will change once we support context
     * sharing.
     */
    private static final String VISTA_DIVISON = "500";
    
    private final VistaDaoFactory fVistaDaoFactory;
    
    /**
     * Constructs an instance.
     * @param vistaDaoFactory
     */
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
        try
        {
            final VistaPerson person = vistaDao.loadVistaPerson(duz);
            return new VistaUserDetails(person);
        }
        catch (final IllegalArgumentException ex)
        {
            throw new UsernameNotFoundException("Invalid user DUZ", ex);
        }
    }
}

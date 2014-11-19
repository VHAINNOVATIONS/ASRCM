package gov.va.med.srcalc.security;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.vista.VistaDao;

import org.springframework.security.core.userdetails.*;

public class VistaUserDetailsService implements UserDetailsService
{
    /**
     * The hardcoded VistA division (500). This is hardcoded for now because this
     * whole authentication implementation will change once we support context
     * sharing.
     */
    private final String VISTA_DIVISON = "500";

    /**
     * Loads the VistaUser for the given DUZ.
     * @throws ConfigurationException if the VistaLinkConnectionFactory could not
     * be located in JNDI.
     */
    @Override
    public VistaUserDetails loadUserByUsername(final String duz) throws UsernameNotFoundException
    {
        final VistaDao vistaDao = new VistaDao(VISTA_DIVISON);
        return new VistaUserDetails(vistaDao.loadVistaPerson(duz));
    }
    
}

package gov.va.med.srcalc.security;

import javax.inject.Inject;

import gov.va.med.srcalc.vista.VistaDao;
import gov.va.med.srcalc.vista.VistaDaoFactory;

import org.springframework.security.core.userdetails.*;

public class VistaUserDetailsService implements UserDetailsService
{
    /**
     * The hardcoded VistA division (500). This is hardcoded for now because this
     * whole authentication implementation will change once we support context
     * sharing.
     */
    private final String VISTA_DIVISON = "500";
    
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
        final VistaDao vistaDao = fVistaDaoFactory.getVistaDao(VISTA_DIVISON);
        return new VistaUserDetails(vistaDao.loadVistaPerson(duz));
    }
    
}

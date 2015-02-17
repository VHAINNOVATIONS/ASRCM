package gov.va.med.srcalc.security;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.vista.VistaDao;

/**
 * <p>Constructs {@link VistaDao}s for a particular VistA division.</p>
 * 
 * <p>Allows {@link VistaUserDetailsService} to construct division-specific
 * {@link VistaDao}s without knowing the implementation.</p>
 */
public interface VistaDaoFactory
{
    /**
     * Returns a {@link VistaDao} for the specific VistA division.
     * @param division e.g., "500"
     * @throws ConfigurationException if the VistA communication is somehow
     * misconfigured
     * @throws IllegalArgumentException if the given division is not known
     */
    public VistaDao getVistaDao(final String division);
}

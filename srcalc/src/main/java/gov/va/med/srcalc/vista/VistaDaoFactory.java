package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.ConfigurationException;

/**
 * Constructs {@link VistaDao}s for a particular VistA division.
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

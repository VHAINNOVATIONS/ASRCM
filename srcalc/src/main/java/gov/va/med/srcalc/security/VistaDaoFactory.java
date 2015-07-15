package gov.va.med.srcalc.security;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.vista.*;

/**
 * <p>Allows clients to construct new VistA DAOs without knowing the implementation.</p>
 * 
 * <p>This technology-agnostic interface facilitates stubbing out the VistA interface
 * in automated tests.</p>
 */
public interface VistaDaoFactory
{
    /**
     * Returns a {@link VistaPersonDao} for the specific VistA division.
     * @param division e.g., "500"
     * @throws ConfigurationException if the VistA communication is somehow
     * misconfigured
     * @throws IllegalArgumentException if the given division is not known
     */
    public VistaPersonDao getVistaPersonDao(final String division);

    /**
     * <p>Returns a {@link VistaPersonDao} that will execute under the context of
     * the current user.</p>
     * 
     * <p>Note that, unlike {@link #getVistaPersonDao(String)}, the division
     * is implied in the current user context.</p>
     * 
     * @throws ConfigurationException if the VistA communication is somehow
     * misconfigured
     */
    public VistaPatientDao getVistaPatientDao();

    /**
     * <p>Returns a {@link VistaSurgeryDao} that will execute under the context of
     * the current user.</p>
     * 
     * <p>Note that, unlike {@link #getVistaPersonDao(String)}, the division
     * is implied in the current user context.</p>
     * 
     * @throws ConfigurationException if the VistA communication is somehow
     * misconfigured
     */
    public VistaSurgeryDao getVistaSurgeryDao();
}

package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.ConfigurationException;

/**
 * <p>Allows clients to construct new VistA DAOs without knowing the implementation.</p>
 * 
 * <p>This technology-agnostic interface facilitates stubbing out the VistA interface
 * in automated tests.</p>
 */
public interface VistaDaoFactory
{
    /**
     * Returns true if this factory can instantiate objects to communicate with the given
     * VistA division, false otherwise.
     */
    public boolean isDivisionKnown(final String division);
    
    /**
     * <p>Returns a VistaAuthenticator that will authenticate users with the given VistA
     * division.</p>
     * 
     * @throws IllegalArgumentException if the division is unknown
     */
    public VistaAuthenticator getAuthenticator(final String division);
    
    /**
     * <p>Returns a {@link VistaPatientDao} that will execute under the context of
     * the current user.</p>
     * 
     * <p>Note that the division is implied in the current user context.</p>
     * 
     * @throws ConfigurationException if the VistA communication is somehow
     * misconfigured
     */
    public VistaPatientDao getVistaPatientDao();

    /**
     * <p>Returns a {@link VistaSurgeryDao} that will execute under the context of
     * the current user.</p>
     * 
     * <p>Note that the division is implied in the current user context.</p>
     * 
     * @throws ConfigurationException if the VistA communication is somehow
     * misconfigured
     */
    public VistaSurgeryDao getVistaSurgeryDao();
}

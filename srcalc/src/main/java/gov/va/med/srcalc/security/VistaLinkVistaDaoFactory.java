package gov.va.med.srcalc.security;

import gov.va.med.srcalc.vista.*;
import gov.va.med.srcalc.vista.vistalink.VistaLinkAuthenticator;
import gov.va.med.srcalc.vista.vistalink.VistaLinkProcedureCaller;
import gov.va.med.srcalc.vista.vistalink.VistaLinkUtil;

/**
 * A VistaDaoFactory that constructs VistALink-implemented DAOs.
 */
public class VistaLinkVistaDaoFactory implements VistaDaoFactory
{
    /**
     * {@inheritDoc}
     * 
     * <p>Returns the same result as {@link VistaLinkUtil#isDivisionKnown(String)}.</p>
     */
    @Override
    public boolean isDivisionKnown(final String division)
    {
        return VistaLinkUtil.isDivisionKnown(division);
    }

    @Override
    public VistaAuthenticator getAuthenticator(final String division)
    {
        return new VistaLinkAuthenticator(division);
    }
    
    @Override
    public VistaPatientDao getVistaPatientDao()
    {
        final VistaUserDetails principal = SecurityUtil.getCurrentPrincipal();

        return new RpcVistaPatientDao(
                new VistaLinkProcedureCaller(principal.getDivision()),
                principal.getDuz());
    }
    
    @Override
    public VistaSurgeryDao getVistaSurgeryDao()
    {
        final VistaUserDetails principal = SecurityUtil.getCurrentPrincipal();
        
        return new RpcVistaSurgeryDao(
                new VistaLinkProcedureCaller(principal.getDivision()),
                principal.getDuz());
    }
}

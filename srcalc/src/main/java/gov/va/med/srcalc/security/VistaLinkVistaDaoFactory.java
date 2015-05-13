package gov.va.med.srcalc.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.va.med.srcalc.vista.*;
import gov.va.med.srcalc.vista.vistalink.VistaLinkProcedureCaller;

public class VistaLinkVistaDaoFactory implements VistaDaoFactory
{
    @Override
    public VistaPersonDao getVistaPersonDao(final String division)
    {
        return new RpcVistaPersonDao(new VistaLinkProcedureCaller(division));
    }
    
    /**
     * Returns the current security principal.
     */
    protected VistaUserDetails getCurrentPrincipal()
    {
        final Authentication authn = SecurityContextHolder.getContext().getAuthentication();
        if (authn == null)
        {
            throw new IllegalStateException("No current security principal");
        }
        return (VistaUserDetails)authn.getPrincipal();
    }
    
    @Override
    public VistaPatientDao getVistaPatientDao()
    {
        final VistaUserDetails principal = getCurrentPrincipal();

        return new RpcVistaPatientDao(
                new VistaLinkProcedureCaller(principal.getDivision()),
                principal.getDuz());
    }
    
    @Override
    public VistaSurgeryDao getVistaSurgeryDao()
    {
        final VistaUserDetails principal = getCurrentPrincipal();
        
        return new RpcVistaSurgeryDao(
                new VistaLinkProcedureCaller(principal.getDivision()),
                principal.getDuz());
    }
}

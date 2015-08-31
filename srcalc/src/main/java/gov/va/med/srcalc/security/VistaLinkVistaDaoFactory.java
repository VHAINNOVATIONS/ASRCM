package gov.va.med.srcalc.security;

import gov.va.med.srcalc.vista.*;
import gov.va.med.srcalc.vista.vistalink.VistaLinkProcedureCaller;

/**
 * A VistaDaoFactory that constructs VistALink-implemented DAOs.
 */
public class VistaLinkVistaDaoFactory implements VistaDaoFactory
{
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

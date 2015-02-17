package gov.va.med.srcalc.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.va.med.srcalc.vista.*;

public class VistaLinkVistaDaoFactory implements VistaDaoFactory
{
    @Override
    public VistaPersonDao getVistaPersonDao(final String division)
    {
        return new VistaLinkVistaPersonDao(division);
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

        return new VistaLinkVistaPatientDao(
                principal.getDivision(), principal.getDuz());
    }
}

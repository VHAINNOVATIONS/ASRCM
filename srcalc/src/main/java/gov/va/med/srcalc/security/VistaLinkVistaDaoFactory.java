package gov.va.med.srcalc.security;

import gov.va.med.srcalc.vista.VistaDao;
import gov.va.med.srcalc.vista.VistaLinkVistaDao;

public class VistaLinkVistaDaoFactory implements VistaDaoFactory
{
    @Override
    public VistaDao getVistaDao(final String division)
    {
        return new VistaLinkVistaDao(division);
    }
}

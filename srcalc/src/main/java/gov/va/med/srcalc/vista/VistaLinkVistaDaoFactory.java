package gov.va.med.srcalc.vista;

public class VistaLinkVistaDaoFactory implements VistaDaoFactory
{
    @Override
    public VistaDao getVistaDao(final String division)
    {
        return new VistaLinkVistaDao(division);
    }
}

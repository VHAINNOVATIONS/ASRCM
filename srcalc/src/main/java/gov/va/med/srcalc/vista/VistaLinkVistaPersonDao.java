package gov.va.med.srcalc.vista;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.med.srcalc.domain.VistaPerson;

/**
 * Implementation of {@link VistaPersonDao} using VistALink.
 */
public class VistaLinkVistaPersonDao implements VistaPersonDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(VistaLinkVistaPersonDao.class);
    
    private final VistaLinkProcedureCaller fProcedureCaller;
    
    public VistaLinkVistaPersonDao(final String division)
    {
        fProcedureCaller = new VistaLinkProcedureCaller(division);
    }

    public VistaPerson loadVistaPerson(final String duz)
    {
        fLogger.debug("Loading VistaPerson for duz {}.", duz);
        
        final List<String> results = fProcedureCaller.doRpc(duz, "SR ASRC USER");
        
        // Get the first index in the array. We don't care about the rest.
        final String userString = results.get(0);
        final VistaPerson person = new VistaPerson(
                fProcedureCaller.getDivision(), duz, userString, "user class not pulled");
        fLogger.debug("Loaded {} from VistA.", person);
        return person;
    }
}

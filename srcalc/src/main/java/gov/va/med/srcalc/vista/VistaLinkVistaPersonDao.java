package gov.va.med.srcalc.vista;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.vistalink.rpc.RpcResponse;

/**
 * Implementation of {@link VistaPersonDao} using VistALink.
 */
public class VistaLinkVistaPersonDao implements VistaPersonDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(VistaLinkVistaPersonDao.class);
    
    private final VistaProcedureCaller fProcedureCaller;
    
    public VistaLinkVistaPersonDao(final String division)
    {
        fProcedureCaller = new VistaProcedureCaller(division);
    }

    public VistaPerson loadVistaPerson(final String duz)
    {
        fLogger.debug("Loading VistaPerson for duz {}.", duz);
        
        final RpcResponse response = fProcedureCaller.doRpc(duz, "SR ASRC USER");
        
        // Get the first index in the array. We don't care about the rest.
        final String userString = response.getResults().split("\n")[0];
        fLogger.debug("Got user: ", userString);
        return new VistaPerson(
                fProcedureCaller.getDivision(), duz, userString, "user class not pulled");
    }
}

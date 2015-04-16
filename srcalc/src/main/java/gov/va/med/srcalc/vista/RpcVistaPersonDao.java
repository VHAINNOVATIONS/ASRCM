package gov.va.med.srcalc.vista;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.med.crypto.VistaKernelHash;
import gov.va.med.crypto.VistaKernelHashCountLimitExceededException;
import gov.va.med.srcalc.domain.VistaPerson;

/**
 * Implementation of {@link VistaPersonDao} using remote procedures.
 */
public class RpcVistaPersonDao implements VistaPersonDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(RpcVistaPersonDao.class);
    
    private final VistaProcedureCaller fProcedureCaller;
    
    public RpcVistaPersonDao(final VistaProcedureCaller procedureCaller)
    {
        fProcedureCaller = procedureCaller;
    }

    public VistaPerson loadVistaPerson(final String duz)
    {
        fLogger.debug("Loading VistaPerson for duz {}.", duz);
        
        final List<String> results = fProcedureCaller.doRpc(duz, RemoteProcedure.GET_USER);
        
        // Get the first index in the array. We don't care about the rest.
        final String userString = results.get(0);
        final VistaPerson person = new VistaPerson(
                fProcedureCaller.getDivision(), duz, userString, "user class not pulled");
        fLogger.debug("Loaded {} from VistA.", person);
        return person;
    }
    
    @Override
    public VistaPerson loadLoggedInPerson(final String loginToken)
    {
        fLogger.debug("Loading VistaPerson for login token {}.", loginToken);
        
        // The FTL client IP.
        final String clientIp = "173.252.156.129";
        
        List<String> results;
        try
        {
            results = fProcedureCaller.doRpcAsProxy(
                    "KAAJEE,PROXY",
                    RemoteProcedure.GET_CCOW_USER,
                    clientIp,
                    "asrcserver",
                    VistaKernelHash.encrypt("~~TOK~~" + loginToken, false));
        }
        catch (VistaKernelHashCountLimitExceededException e)
        {
            throw new RuntimeException("should not be thrown", e);
        }
        
        fLogger.debug("Got CCOW user info: {}", results);
        
        return new VistaPerson(
                fProcedureCaller.getDivision(), results.get(0), results.get(1), "user class not pulled");
    }
}

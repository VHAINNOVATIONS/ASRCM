package gov.va.med.srcalc.vista;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.VistaPerson;

/**
 * Implementation of {@link VistaPersonDao} using remote procedures.
 */
public class RpcVistaPersonDao implements VistaPersonDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcVistaPersonDao.class);
    
    private final VistaProcedureCaller fProcedureCaller;
    
    /**
     * Constructs an instance.
     * @param procedureCaller used to call remote procedures from VistA
     */
    public RpcVistaPersonDao(final VistaProcedureCaller procedureCaller)
    {
        fProcedureCaller = procedureCaller;
    }
    
    /**
     * Loads the user's single Provider Type, if present.
     */
    private Optional<String> loadProviderType(final String duz)
    {
        final List<String> personClassResults = fProcedureCaller.doRpc(
                duz, RemoteProcedure.GET_USER_PERSON_CLASSES);
        
        if (!personClassResults.isEmpty())
        {
            // The RPC returns an array, but a user should only have one non-expired
            // Person Class in VistA.
            if (personClassResults.size() > 1)
            {
                LOGGER.warn(
                        "VistA unexpectedly returned more than one person class for " +
                        " {}@{}: {}. All but the first one will be discarded.",
                        duz,
                        fProcedureCaller.getDivision(),
                        personClassResults);
            }
            
            return Optional.of(personClassResults.get(0));
        }
        else
        {
            return Optional.absent();
        }
    }

    public VistaPerson loadVistaPerson(final String duz)
    {
        LOGGER.debug("Loading VistaPerson for duz {}.", duz);
        
        final List<String> userResults = fProcedureCaller.doRpc(
                duz, RemoteProcedure.GET_USER);
        
        // Get the first index in the array. We don't care about the rest.
        final String userString = userResults.get(0);
        final VistaPerson person = new VistaPerson(
                fProcedureCaller.getDivision(),
                duz,
                userString,
                loadProviderType(duz));
        LOGGER.debug("Loaded {} from VistA.", person);
        return person;
    }
}

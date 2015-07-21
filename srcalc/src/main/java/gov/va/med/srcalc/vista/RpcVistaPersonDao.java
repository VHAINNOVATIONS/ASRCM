package gov.va.med.srcalc.vista;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.VistaPerson;

/**
 * Implementation of {@link VistaPersonDao} using remote procedures.
 */
public class RpcVistaPersonDao implements VistaPersonDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcVistaPersonDao.class);
    
    private final VistaProcedureCaller fProcedureCaller;
    
    public RpcVistaPersonDao(final VistaProcedureCaller procedureCaller)
    {
        fProcedureCaller = procedureCaller;
    }

    public VistaPerson loadVistaPerson(final String duz)
    {
        LOGGER.debug("Loading VistaPerson for duz {}.", duz);
        
        final List<String> userResults = fProcedureCaller.doRpc(
                duz, RemoteProcedure.GET_USER);
        final List<String> personClassResults = fProcedureCaller.doRpc(
                duz, RemoteProcedure.GET_USER_PERSON_CLASSES);
        
        // Get the first index in the array. We don't care about the rest.
        final String userString = userResults.get(0);
        final VistaPerson person = new VistaPerson(
                fProcedureCaller.getDivision(),
                duz,
                userString,
                // Transform from a List to a Set, but the RPC result doesn't need any
                // more than that.
                ImmutableSet.copyOf(personClassResults));
        LOGGER.debug("Loaded {} from VistA.", person);
        return person;
    }
}

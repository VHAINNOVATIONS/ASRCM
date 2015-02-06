package gov.va.med.srcalc.ccow;

import java.io.IOException;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

/**
 * A Client to a CMA Component (e.g., a ContextManager or ContextParticipant).
 */
public class ComponentClient
{
    private final ComponentLocation fLocation;
    private final String fInterfaceName;
    
    public ComponentClient(final ComponentLocation location, final String interfaceName)
    {
        fLocation = location;
        fInterfaceName = interfaceName;
    }

    /**
     * Executes a request against the configured interface and given method.
     * @param method the method to execute
     * @param otherArguments all other arguments for the method
     * @return a Map of output parameters
     * @throws IOException if an HTTP error occurs
     * @throws CmaException if the component returns a CMA Exception
     */
    public MultivaluedMap<String, String> request(
            final String method, final Argument... otherArguments)
            throws IOException, CmaException
    {
        WebTarget target = fLocation.makeWebTarget()
                .queryParam("interface", fInterfaceName)
                .queryParam("method", method);
        for (final Argument argument : otherArguments)
        {
            target = target.queryParam(argument.getKey(), argument.getValue());
        }
        final Response response = target
                .request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .get();
        
        if (response.getStatus() != Status.OK.getStatusCode())
        {
            throw new IOException(String.format(
                    "Component return non-OK (status %d).",
                    response.getStatus()));
        }
        
        final MultivaluedMap<String, String> responseOutputs =
                response.readEntity(CcowUtils.MULTI_MAP_TYPE);
        
        if (responseOutputs.containsKey("exception"))
        {
            throw new CmaException(
                    responseOutputs.getFirst("exception"),
                    responseOutputs.getFirst("exceptionMessage"));
        }
        
        return responseOutputs;
    }
    
    public static class Argument
    {
        private final String fKey;
        private final Object fValue;
        
        public Argument(final String key, final Object value)
        {
            fKey = key;
            fValue = value;
        }
        
        /**
         * Convenience factory method for fluent construction.
         */
        public static Argument of(final String key, final Object value)
        {
            return new Argument(key, value);
        }

        public String getKey()
        {
            return fKey;
        }

        public Object getValue()
        {
            return fValue;
        }
    }
}

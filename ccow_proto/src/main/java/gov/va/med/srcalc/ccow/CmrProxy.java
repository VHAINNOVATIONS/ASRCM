package gov.va.med.srcalc.ccow;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A proxy object to the ContextManagementRegistry.
 */
public class CmrProxy
{
    private static final Logger fLogger = LoggerFactory.getLogger(CmrProxy.class);
    
    /**
     * The well-known address of the ContextManagementRegistry.
     */
    public static final URI CMR_ADDRESS;
    
    static
    {
        try
        {
            CMR_ADDRESS = new URI("http://localhost:2116/");
        }
        catch (URISyntaxException e)
        {
            throw new ExceptionInInitializerError("Invalid configured URI");
        }
    }
    
    /**
     * The interface name of the ContextManagementRegistry.
     */
    public static final String INTERFACE_NAME = "ContextManagementRegistry";
    
    /**
     * The method name of ContextManagementRegistry.Locate.
     */
    public static final String METHOD_LOCATE = "Locate";
    
    /**
     * The CMA spec version.
     */
    public static final String VERSION = "1.5";
    
    /**
     * The well-known component name of the ContextManager.
     */
    public static final String COMPNENT_NAME_CM = "CCOW.ContextManager";

    /**
     * Locates the ContextManager.
     * @param participantUrl
     */
    public static ComponentLocation locate(final String participantUrl)
        throws IOException
    {
        fLogger.info("Locating ContextManager for client: {}", participantUrl);

        final Client client = ClientBuilder.newClient()
                // The Vergence CMR replies with text/html.
                .register(new ContentTypeOverrideFilter());
        final WebTarget target = client.target(CMR_ADDRESS);
        final Response response = target
                .queryParam("interface", INTERFACE_NAME)
                .queryParam("method", METHOD_LOCATE)
                .queryParam("version", VERSION)
                .queryParam("componentName", COMPNENT_NAME_CM)
                .queryParam("contextParticipant", participantUrl)
                .request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .get();
        
        if (response.getStatus() != Status.OK.getStatusCode())
        {
            throw new IOException(String.format(
                    "Could not query CMR for ContextManager (status %d).",
                    response.getStatus()));
        }

        final MultivaluedMap<String, String> responseValues =
                response.readEntity(CcowUtils.MULTI_MAP_TYPE);
        
        return new ComponentLocation(
                responseValues.getFirst("componentUrl"),
                responseValues.getFirst("componentParameters"),
                responseValues.getFirst("site"));
    }
}

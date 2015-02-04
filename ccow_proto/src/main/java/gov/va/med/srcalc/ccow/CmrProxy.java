package gov.va.med.srcalc.ccow;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

/**
 * A proxy object to the ContextManagementRegistry.
 */
public class CmrProxy
{
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

    public static ComponentLocation queryLocator()
    {
        final Client client = ClientBuilder.newClient().register(new ContentTypeOverrideFilter());
        final WebTarget target = client.target(CMR_ADDRESS);
        final Response response = target
                .queryParam("interface", INTERFACE_NAME)
                .queryParam("method", METHOD_LOCATE)
                .queryParam("version", VERSION)
                .queryParam("componentName", COMPNENT_NAME_CM)
                .queryParam("contextParticipant", "foo")
                .request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .get();
        
        final MultivaluedMap<String, String> responseValues =
                response.readEntity(MultivaluedMap.class);
        
        return new ComponentLocation(
                responseValues.getFirst("componentUrl"),
                responseValues.getFirst("componentParameters"),
                responseValues.getFirst("site"));
    }
}

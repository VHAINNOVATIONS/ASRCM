package gov.va.med.srcalc.ccow;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * A mock ContextManagementRegistry that responds to a Locate request with
 * canned data.
 */
@Path("/")
public class MockRegistry
{
    /**
     * The canned Context Manager URL.
     */
    public static final String CANNED_CM_URL =
            "http://www.sentillion.com/duke/contextManager/";
    
    /**
     * The canned Context Manager parameters.
     */
    public static final String CANNED_CM_PARAMETERS =
            "id=987654321&domain=duke.edu";
    
    /**
     * The canned Context Manager site.
     */
    public static final String CANNED_CM_SITE = "www.duke.edu";
    
    /**
     * The CCOW spec does not use HTTP paths very well and all methods are
     * called via the root path, with two parameters to distinguish them. This
     * method is a "front controller" delegating to the real methods.
     */
    @GET
    @Produces(MediaType.APPLICATION_FORM_URLENCODED) // TODO: text/html per Vergence
    public MultivaluedMap<String, String> frontController(
            @QueryParam("interface") final String iface,
            @QueryParam("method") final String method,
            @Context final UriInfo uriInfo)
    {
        if (
                CmrProxy.INTERFACE_NAME.equalsIgnoreCase(iface) &&
                CmrProxy.METHOD_LOCATE.equalsIgnoreCase(method))
        {
            final MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
            return locate(
                    params.getFirst("componentName"),
                    params.getFirst("version"),
                    params.getFirst("descriptiveData"),
                    params.getFirst("contextParticipant")
                    );
        }
        else
        {
            throw new RuntimeException("NotIplemented");
        }
    }

    public MultivaluedMap<String, String> locate(
            final String componentName,
            final String version,
            final String descriptiveData,
            final String contextParticipant)
    {
        if (
                CmrProxy.VERSION.equalsIgnoreCase(version) &&
                CmrProxy.COMPNENT_NAME_CM.equalsIgnoreCase(componentName) &&
                !contextParticipant.isEmpty())
        {
            MultivaluedHashMap<String, String> responseMap = new MultivaluedHashMap<>();
            responseMap.add("componentUrl", CANNED_CM_URL);
            responseMap.add("componentParameters", CANNED_CM_PARAMETERS);
            responseMap.add("site", CANNED_CM_SITE);
            return responseMap;
        }
        else
        {
            throw new RuntimeException("bad parameter value");
        }
    }
}

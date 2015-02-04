package gov.va.med.srcalc.ccow.proto;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of the CCOW CMA ContextParticipant interface for the
 * prototype.
 */
@Path("/")
public class ProtoContextParticipant
{
    /**
     * The interface name of the ContextParticipant.
     */
    public static final String INTERFACE_NAME = "ContextParticipant";
    
    
    private static final Logger fLogger = LoggerFactory.getLogger(ProtoContextParticipant.class);
    
    public enum Methods
    {
        ContextChangesPending,
        ContextChangesAccepted,
        ContextChangesCanceled,
        CommonContextTerminated,
        Ping
    }

    /**
     * CMA determines methods not by URL but by query parameters, so this method
     * acts as a front controller to delegate a request to the proper method.
     * @param iface must always be {@link #INTERFACE_NAME}.
     * @param method
     */
    @GET
    public MultivaluedMap<String, String> frontController(
            @QueryParam("interface") final String iface,
            @QueryParam("method") final String method,
            @Context final UriInfo uriInfo)
    {
        if (!INTERFACE_NAME.equalsIgnoreCase(iface))
        {
            throw new WebApplicationException(Response
                    .status(Status.NOT_FOUND)
                    .entity("exception=NotImplemented&exceptionMessage=Interface not implemented")
                    .build());
        }
        
        if (method.equals(Methods.ContextChangesPending.name()))
        {
            return onContextChangesPending(
                    uriInfo.getQueryParameters().getFirst("contextCoupon"));
        }
        else if (method.equals(Methods.ContextChangesAccepted.name()))
        {
            onContextChangesAccepted(
                    uriInfo.getQueryParameters().getFirst("contextCoupon"));
            
            return new MultivaluedHashMap<>();
        }
        else if (method.equals(Methods.ContextChangesCanceled.name()))
        {
            onContextChangesCanceled(
                    uriInfo.getQueryParameters().getFirst("contextCoupon"));
            
            return new MultivaluedHashMap<>();
        }
        else if (method.equals(Methods.CommonContextTerminated.name()))
        {
            onCommonContextTerminated();
            
            return new MultivaluedHashMap<>();
        }
        else if (method.equals(Methods.Ping.name()))
        {
            onPing();
            
            return new MultivaluedHashMap<>();
        }
        else
        {
            throw new WebApplicationException(Response
                    .status(Status.NOT_FOUND)
                    .entity("exception=NotImplemented&exceptionMessage=Method not implemented")
                    .build());
        }
    }

    public MultivaluedMap<String, String> onContextChangesPending(
            final String contextCoupon)
    {
        fLogger.info("Accepting pending context change {}", contextCoupon);

        final MultivaluedMap<String, String> response = new MultivaluedHashMap<>();
        response.add("decision", "accept");
        response.add("reason", "");
        return response;
    }
    
    public void onContextChangesAccepted(final String contextCoupon)
    {
        fLogger.info("Context changed! New coupon: {}", contextCoupon);
    }
    
    public void onContextChangesCanceled(final String contextCoupon)
    {
        // We don't care.
    }
    
    public void onCommonContextTerminated()
    {
        fLogger.info("CommonContext terminated. Interesting.");
    }
    
    public void onPing()
    {
        // No action necessary.
    }
    
}

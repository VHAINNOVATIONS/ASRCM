package gov.va.med.srcalc.ccow;

import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;

import gov.va.med.srcalc.ccow.ComponentClient.Argument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A proxy object to a ContextManager.
 */
public class CmProxy
{
    public static final String INTERFACE_NAME = "ContextManager";

    private static final Logger fLogger = LoggerFactory.getLogger(CmProxy.class);
    
    private final ComponentClient fClient;
    
    /**
     * The ContextManager methods that this proxy supports. (Not all are
     * supported).
     */
    public enum Methods
    {
        GetMostRecentContextCoupon,
        JoinCommonContext,
        LeaveCommonContext
    }
    
    public CmProxy(final ComponentLocation location)
    {
        fLogger.debug("Creating CmProxy for: {}", location);
        fClient = new ComponentClient(location, INTERFACE_NAME);
    }
    
    /**
     * Executes the CMA method JoinCommonContext.
     * @return the participant coupon
     * @throws IOException 
     */
    public String joinCommonContext(
            final String applicationName,
            final String participantUrl,
            final boolean survey,
            final boolean wait)
                    throws IOException, CmaException
    {
        fLogger.debug("Joining context...");
        
        final MultivaluedMap<String, String> responseValues = fClient.request(
                Methods.JoinCommonContext.name(),
                Argument.of("applicationName", applicationName),
                Argument.of("contextParticipant", participantUrl),
                Argument.of("survey", survey),
                Argument.of("wait", wait));
        
        return responseValues.getFirst("participantCoupon");
    }
    
    /**
     * Executes the CMA method GetMostRecentContextCoupon.
     * @return the most recent context coupon
     */
    public String getMostRecentContextCoupon() throws IOException, CmaException
    {
        final MultivaluedMap<String, String> responseValues = fClient.request(
                Methods.GetMostRecentContextCoupon.name());
        
        return responseValues.getFirst("contextCoupon");
    }
    
    /**
     * Executes the CMA method LeaveCommonContext.
     */
    public void leaveCommonContext(final String participantCoupon)
        throws IOException, CmaException
    {
        fLogger.debug("Leaving context...");
        
        fClient.request(
                Methods.LeaveCommonContext.name(),
                Argument.of("participantCoupon", participantCoupon));
    }
}

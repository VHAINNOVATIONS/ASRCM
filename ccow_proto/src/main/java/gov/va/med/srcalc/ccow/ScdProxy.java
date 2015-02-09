package gov.va.med.srcalc.ccow;

import gov.va.med.srcalc.ccow.ComponentClient.Argument;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A proxy object to a SecureContextData interface..
 */
public class ScdProxy
{
    static final String INTERFACE_NAME = "SecureContextData";
    
    private static final Logger fLogger = LoggerFactory.getLogger(ScdProxy.class);
    
    private final ComponentClient fClient;
    
    public enum Methods
    {
        GetItemValues
    }
    
    public ScdProxy(final ComponentLocation location)
    {
        fLogger.debug("Creating ScdProxy for: {}", location);
        fClient = new ComponentClient(location, INTERFACE_NAME);
    }
    
    public GetItemValuesResponse getItemValues(
            final String participantCoupon,
            final List<String> itemNames,
            final boolean onlyChanges,
            final String contextCoupon,
            final String appSignature)
                    throws IOException, CmaException
    {
        fLogger.debug("Getting item values...");
        
        final MultivaluedMap<String, String> responseValues = fClient.request(
                Methods.GetItemValues.name(),
                Argument.of("participantCoupon", participantCoupon),
                Argument.of("itemNames", CcowUtils.listToCmaArray(itemNames)),
                Argument.of("onlyChanges", onlyChanges),
                Argument.of("contextCoupon", contextCoupon),
                Argument.of("appSignature", appSignature));
        
        return new GetItemValuesResponse(
                CcowUtils.cmaArrayToList(responseValues.getFirst("itemValues")),
                responseValues.getFirst("managerSignature"));
    }
    
    public static class GetItemValuesResponse
    {
        private final List<String> fItemValues;
        private final String fManagerSignature;
        
        public GetItemValuesResponse(
                final List<String> itemValues, final String managerSignature)
        {
            fItemValues = itemValues;
            fManagerSignature = managerSignature;
        }

        public List<String> getItemValues()
        {
            return fItemValues;
        }

        public String getManagerSignature()
        {
            return fManagerSignature;
        }
    }
}

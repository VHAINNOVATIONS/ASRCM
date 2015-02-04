package gov.va.med.srcalc.ccow;

import java.io.IOException;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

/**
 * Overrides the incoming Content-Type to call it "application/x-www-form-urlencoded".
 * Useful because Vergence always sends back "text/html" even when it isn't.
 */
public class ContentTypeOverrideFilter implements ClientResponseFilter
{
    
    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException
    {
        // If the response was "OK", Vergence sends Content-Type: text/html even
        // though the data is actually "application/x-www-form-urlencoded".
        if (responseContext.getStatus() == 200)
        {
            responseContext.getHeaders().putSingle(
                    "Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
        }
    }
    
}

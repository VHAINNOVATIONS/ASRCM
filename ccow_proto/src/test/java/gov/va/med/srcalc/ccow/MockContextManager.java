package gov.va.med.srcalc.ccow;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Mock CMA ContextManager, implemented as a Grizzy {@link HttpHandler}.
 */
public class MockContextManager extends HttpHandler
{
    private static final Logger fLogger = LoggerFactory.getLogger(MockContextManager.class);
    
    public static final String EXCEPTION_MESSAGE =
            "exception=UnknownSession&exceptionMessage=DesktopId+%3D+PHS-XPVIEW-07_1423162920628_95+orgid+%3D+USDVA+did+not+map+to+an+existing+context+manager.++The+request+was+for%3A+ContextManager%3A%3AGetMostRecentContextCoupon";
    
    @Override
    public void service(final Request request, final Response response)
        throws IOException
    {
        fLogger.debug("Handling request");
        
        final String iface = request.getParameter("interface");
        final String method = request.getParameter("method");
        
        if (
                CmProxy.INTERFACE_NAME.equals(iface) &&
                CmProxy.Methods.GetMostRecentContextCoupon.name().equals(method))
        {
            response.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            response.getWriter().write(EXCEPTION_MESSAGE);
        }
        else
        {
            response.setStatus(HttpStatus.BAD_REQUEST_400);
        }
    }
    
}

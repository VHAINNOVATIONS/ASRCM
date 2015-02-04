package gov.va.med.srcalc.ccow;

import java.io.IOException;
import java.io.PrintStream;

import org.simpleframework.http.*;
import org.simpleframework.http.core.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A mock Vergence ContextManagementRegistry that responds to a Locate request
 * with canned data.
 */
public class MockVergenceCmr implements Container
{
    private static final Logger fLogger = LoggerFactory.getLogger(MockVergenceCmr.class);
    
    /**
     * The canned Context Manager URL.
     */
    public static final String CANNED_CM_URL =
            "http://172.19.200.128/USDVA/CM/AC13C880";

    /**
     * The canned Context Manager parameters.
     */
    public static final String CANNED_CM_PARAMETERS =
            "desktopId=PHS-XPVIEW-07_1422974974126_83&backChannelId=PHS-XPVIEW-07_1422974974126_83-0&cpId=4";

    /**
     * The canned Context Manager site.
     */
    public static final String CANNED_CM_SITE = "http://www.USDVA.com";

    private static String reply =
            "componentUrl=http%3a%2f%2f172.19.200.128%2fUSDVA%2fCM%2fAC13C880&componentParameters=desktopId%3dPHS-XPVIEW-07_1422974974126_83%26backChannelId%3dPHS-XPVIEW-07_1422974974126_83-0%26cpId%3d4&site=http%3a%2f%2fwww.USDVA.com";
    
    @Override
    public void handle(final Request request, final Response response)
    {
        fLogger.debug("Handling request");
        
        final Query params = request.getQuery();
        final String iface = params.get("interface");
        final String method = params.get("method");
        
        if (
                CmrProxy.INTERFACE_NAME.equalsIgnoreCase(iface) &&
                CmrProxy.METHOD_LOCATE.equalsIgnoreCase(method))
        {
            locate(
                    params.get("componentName"),
                    params.get("version"),
                    params.get("descriptiveData"),
                    params.get("contextParticipant"),
                    response);
        }
        
    }

    public void locate(
            final String componentName,
            final String version,
            final String descriptiveData,
            final String contextParticipant,
            final Response response)
    {
        if (
                CmrProxy.VERSION.equalsIgnoreCase(version) &&
                CmrProxy.COMPNENT_NAME_CM.equalsIgnoreCase(componentName) &&
                !contextParticipant.isEmpty())
        {
            response.setValue("Content-Type", "text/html");
            try
            {
                final PrintStream body = response.getPrintStream();
                body.print(reply);
                body.close();
            }
            catch (final IOException e)
            {
                fLogger.error("Failed to write HTTP response.", e);
            }
        }
        else
        {
            throw new RuntimeException("bad parameter value");
        }
    }
    
}

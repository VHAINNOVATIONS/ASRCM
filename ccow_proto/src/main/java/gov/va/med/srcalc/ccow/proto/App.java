package gov.va.med.srcalc.ccow.proto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.ProcessingException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.med.srcalc.ccow.*;

public class App
{
    private static final Logger fLogger = LoggerFactory.getLogger(App.class);

    private static final URI baseCpUri;
    
    static
    {
        try
        {
            baseCpUri = new URI("http://localhost:9998/contextParticipant");
        }
        catch (final URISyntaxException ex)
        {
            throw new ExceptionInInitializerError("Invalid base URI configured.");
        }
    }
    
    private static HttpServer fServer;
    
    private static ResourceConfig createApp()
    {
        return new ResourceConfig(ProtoContextParticipant.class);
    }

    private static void startContextParticipantServer()
    {
        fLogger.info("Starting ContextParticipant server...");

        try
        {
            fServer = GrizzlyHttpServerFactory.createHttpServer(
                            baseCpUri, createApp());
        }
        catch (ProcessingException e)
        {
            throw new RuntimeException("I dunno what this is.");
        }
    }
    
    private static void stopContextParticipantServer()
    {
        fLogger.info("Shutting down ContextParticipant server...");
        fServer.stop();
    }

    public static void main(String[] args) throws Exception
    {
        // java.util.logging format is ugly, but too hard to improve. So just
        // set the "org" logger to WARNING so that Grizzly, etc. won't log much.
        java.util.logging.Logger.getLogger("org").setLevel(Level.WARNING);

        startContextParticipantServer();
        
        final ComponentLocation cmLocation = CmrProxy.locate(baseCpUri.toString());
        final CmProxy contextManager = new CmProxy(cmLocation);
        final ScdProxy secureContextData = new ScdProxy(cmLocation);
        
        final String participantCoupon = contextManager.joinCommonContext(
                "ccow_proto", baseCpUri.toString(), false, false);
        fLogger.info("Joined context session as participant {}", participantCoupon);
        try
        {
            final String recentContextCoupon = contextManager.getMostRecentContextCoupon();
            fLogger.info("Most recent coupon: {}", recentContextCoupon);
            final List<String> itemValues = secureContextData.getItemValues(
                    "0", Arrays.asList("Patient.*", "User.*"), false, recentContextCoupon, "")
                    .getItemValues();
            fLogger.info("Patient and User Context Data: {}", itemValues);
        }
        finally
        {
            contextManager.leaveCommonContext(participantCoupon);
            
            stopContextParticipantServer();
        }
    }
}

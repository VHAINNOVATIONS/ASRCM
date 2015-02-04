package gov.va.med.srcalc.ccow.proto;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.ProcessingException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.med.srcalc.ccow.CmrProxy;
import gov.va.med.srcalc.ccow.ComponentLocation;

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
        startContextParticipantServer();
        
        final ComponentLocation cmLocation = CmrProxy.locate(baseCpUri.toString());
        System.out.println("CM Location: " + cmLocation);
        
        stopContextParticipantServer();
    }
}

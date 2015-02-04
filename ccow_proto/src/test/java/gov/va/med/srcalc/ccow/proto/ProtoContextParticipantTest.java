package gov.va.med.srcalc.ccow.proto;

import static org.junit.Assert.*;

import javax.ws.rs.core.*;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class ProtoContextParticipantTest extends JerseyTest
{
    @Override
    protected Application configure()
    {
        return new ResourceConfig(ProtoContextParticipant.class);
    }
    
    @Test
    public final void testOnContextChangesPending()
    {
        final MultivaluedMap<String, String> response = target("/")
            .queryParam("interface", ProtoContextParticipant.INTERFACE_NAME)
            .queryParam("method", ProtoContextParticipant.Methods.ContextChangesPending.name())
            .queryParam("contextCoupon", "1")
            .request().get(MultivaluedMap.class);
        
        assertEquals("accept", response.getFirst("decision"));
        assertEquals("", response.getFirst("reason"));
    }
    
    @Test
    public final void testOnContextChangesAccepted()
    {
        final Response response = target("/")
            .queryParam("interface", ProtoContextParticipant.INTERFACE_NAME)
            .queryParam("method", ProtoContextParticipant.Methods.ContextChangesAccepted.name())
            .queryParam("contextCoupon", "1")
            .request().get();
        
        // There is no response content required: just check the status.
        assertEquals(200, response.getStatus());
    }
    
    @Test
    public final void testOnContextChangesCanceled()
    {
        final Response response = target("/")
            .queryParam("interface", ProtoContextParticipant.INTERFACE_NAME)
            .queryParam("method", ProtoContextParticipant.Methods.ContextChangesCanceled.name())
            .queryParam("contextCoupon", "1")
            .request().get();
        
        // There is no response content required: just check the status.
        assertEquals(200, response.getStatus());
    }
    
    @Test
    public final void testOnCommonContextTerminated()
    {
        final Response response = target("/")
            .queryParam("interface", ProtoContextParticipant.INTERFACE_NAME)
            .queryParam("method", ProtoContextParticipant.Methods.CommonContextTerminated.name())
            .request().get();
        
        // There is no response content required: just check the status.
        assertEquals(200, response.getStatus());
    }
    
    @Test
    public final void testOnPing()
    {
        final Response response = target("/")
            .queryParam("interface", ProtoContextParticipant.INTERFACE_NAME)
            .queryParam("method", ProtoContextParticipant.Methods.Ping.name())
            .request().get();
        
        // There is no response content required: just check the status.
        assertEquals(200, response.getStatus());
    }
    
}

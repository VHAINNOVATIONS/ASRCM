package gov.va.med.srcalc.ccow.proto;

import static org.junit.Assert.*;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MultivaluedMap;

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
    }
    
}

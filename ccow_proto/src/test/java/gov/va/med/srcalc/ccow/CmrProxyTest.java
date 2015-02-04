package gov.va.med.srcalc.ccow;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.junit.*;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

public class CmrProxyTest
{
    private static Connection fMockRegistryConnection;
    
    @BeforeClass
    public static void setupMockRegistry() throws IOException
    {
        final MockVergenceCmr mockCmr = new MockVergenceCmr();
        final Server server = new ContainerServer(mockCmr);
        fMockRegistryConnection = new SocketConnection(server);
        SocketAddress address = new InetSocketAddress(CmrProxy.CMR_ADDRESS.getPort());
        fMockRegistryConnection.connect(address);
    }
    
    @AfterClass
    public static void closeMockRegistry() throws IOException
    {
        if (fMockRegistryConnection != null)
        {
            fMockRegistryConnection.close();
        }
    }

    @Test
    public final void testQueryLocator()
    {
        final ComponentLocation location = CmrProxy.queryLocator();
        assertEquals(MockVergenceCmr.CANNED_CM_URL, location.getUrl());
        assertEquals(MockVergenceCmr.CANNED_CM_PARAMETERS, location.getParameters());
        assertEquals(MockVergenceCmr.CANNED_CM_SITE, location.getSite());
        assertThat(location.toString(), containsString(MockVergenceCmr.CANNED_CM_PARAMETERS));
    }
    
}

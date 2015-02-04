package gov.va.med.srcalc.ccow;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.junit.*;

public class CmrProxyTest
{
    private static HttpServer fMockRegistryServer;
    
    @BeforeClass
    public static void setupMockRegistry() throws IOException
    {
        final MockVergenceCmr mockCmr = new MockVergenceCmr();
        fMockRegistryServer = new HttpServer();
        fMockRegistryServer.addListener(new NetworkListener(
                "mockRegistryListener",
                "localhost",
                CmrProxy.CMR_ADDRESS.getPort()));
        fMockRegistryServer.getServerConfiguration().addHttpHandler(mockCmr, "/");
        fMockRegistryServer.start();
    }
    
    @AfterClass
    public static void closeMockRegistry() throws IOException
    {
        if (fMockRegistryServer != null)
        {
            fMockRegistryServer.stop();
        }
    }

    @Test
    public final void testQueryLocator() throws Exception
    {
        final ComponentLocation location = CmrProxy.locate("foo");
        assertEquals(MockVergenceCmr.CANNED_CM_URL, location.getUrl());
        assertEquals(MockVergenceCmr.CANNED_CM_PARAMETERS, location.getParameters());
        assertEquals(MockVergenceCmr.CANNED_CM_SITE, location.getSite());
        assertThat(location.toString(), containsString(MockVergenceCmr.CANNED_CM_PARAMETERS));
    }
    
}

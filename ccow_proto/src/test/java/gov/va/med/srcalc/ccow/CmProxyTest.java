package gov.va.med.srcalc.ccow;

import java.io.IOException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.junit.*;

public class CmProxyTest
{
    private static HttpServer fMockRegistryServer;
    
    @BeforeClass
    public static void setupClass() throws IOException
    {
        final MockContextManager mockCm = new MockContextManager();
        fMockRegistryServer = new HttpServer();
        fMockRegistryServer.addListener(new NetworkListener(
                "mockContextManager", "localhost", 9997));
        fMockRegistryServer.getServerConfiguration().addHttpHandler(
                mockCm, "/contextManager");
        fMockRegistryServer.start();
    }
    
    @AfterClass
    public static void tearDownClass() throws IOException
    {
        if (fMockRegistryServer != null)
        {
            fMockRegistryServer.stop();
        }
    }

    @Test(expected = CmaException.class)
    public final void testJoinCommonContextException() throws Exception
    {
        final CmProxy cmp = new CmProxy(new ComponentLocation(
                "http://localhost:9997/contextManager",
                "",
                "http://www.ccow.org"));
        
        cmp.getMostRecentContextCoupon();
    }
    
}

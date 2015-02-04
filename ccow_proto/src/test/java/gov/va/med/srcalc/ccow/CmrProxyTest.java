package gov.va.med.srcalc.ccow;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

/**
 * We use the Jersey Test Framework backwards: instead of testing a Resource,
 * we're actually testing the client but using the test framework to instantiate
 * a Resource in a well-known location.
 */
public class CmrProxyTest extends JerseyTest
{
    @Override
    protected Application configure()
    {
        // Set the port to the well-known CCOW CMR port.
        System.setProperty(
                TestProperties.CONTAINER_PORT,
                Integer.toString(CmrProxy.CMR_ADDRESS.getPort()));
        return new ResourceConfig(MockRegistry.class);
    }

    @Test
    public final void testQueryLocator()
    {
        final ComponentLocation location = CmrProxy.queryLocator();
        assertEquals(MockRegistry.CANNED_CM_URL, location.getUrl());
        assertEquals(MockRegistry.CANNED_CM_PARAMETERS, location.getParameters());
        assertEquals(MockRegistry.CANNED_CM_SITE, location.getSite());
        assertThat(location.toString(), containsString(MockRegistry.CANNED_CM_PARAMETERS));
    }
    
}

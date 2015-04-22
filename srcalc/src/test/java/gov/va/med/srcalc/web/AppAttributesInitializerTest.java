package gov.va.med.srcalc.web;

import static org.mockito.Mockito.*;
import gov.va.med.srcalc.SrcalcInfo;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.junit.Test;

public class AppAttributesInitializerTest
{
    /**
     * Tests {@link AppAttributesInitializer#contextInitialized(ServletContextEvent)},
     * verifying that the appInfo attribute is added.
     */
    @Test
    public final void testContextInitialized() throws Exception
    {
        final AppAttributesInitializer aai = new AppAttributesInitializer();
        final ServletContext sc = WebMocks.mockServletContext();
        aai.contextInitialized(new ServletContextEvent(sc));
        
        // The mock ServletContext doesn't actually remember any set attributes,
        // so just verify the right method was called instead.
        verify(sc).setAttribute(
               eq(AppAttributesInitializer.ATTRIBUTE_APP_INFO),
               any(SrcalcInfo.class));
    }
    
    /**
     * No behavior to test: just make sure it doesn't throw an Exception or
     * something because it will be called by the container.
     */
    @Test
    public final void testContextDestroyed() throws Exception
    {
        final AppAttributesInitializer aai = new AppAttributesInitializer();
        aai.contextDestroyed(new ServletContextEvent(WebMocks.mockServletContext()));
    }
    
}

package gov.va.med.srcalc.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.va.med.srcalc.SrcalcInfo;

import java.io.FileNotFoundException;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.junit.Test;

/**
 * Tests the {@link WebUtils} class.
 */
public class WebUtilsTest
{

    @Test
    public final void testReadWebappManifest() throws Exception
    {
        // Behavior verification
        final Manifest manifest = WebUtils.readWebappManifest(WebMocks.mockServletContext());
        assertEquals(4, manifest.getMainAttributes().size());
        assertEquals("srcalc-test", manifest.getMainAttributes().getValue("Implementation-Title"));
    }
    
    @Test
    public final void testReadSrcalcInfo() throws Exception
    {
        // Behavior verification
        final SrcalcInfo srcalcInfo = WebUtils.readSrcalcInfo(WebMocks.mockServletContext());
        assertEquals("srcalc-test", srcalcInfo.getLongName());
        assertEquals("0.1.0-test", srcalcInfo.getVersion());
    }
    
    @Test(expected = FileNotFoundException.class)
    public final void testReadWebappManifestNotFound() throws Exception
    {
        // Setup
        final ServletContext mockServletContext = mock(ServletContext.class);
        // getResourceAsStream() returns null if no such resource exists
        when(mockServletContext.getResourceAsStream(anyString())).thenReturn(null);
        
        // Behavior verification
        WebUtils.readWebappManifest(mockServletContext);
    }
    
}

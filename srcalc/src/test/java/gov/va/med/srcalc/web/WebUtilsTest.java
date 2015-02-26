package gov.va.med.srcalc.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class WebUtilsTest
{
    @Test
    public final void testReadWebappManifest() throws Exception
    {
        // Setup
        final ServletContext mockServletContext = mock(ServletContext.class);
        final ClassPathResource manifestResource =
                // Find it in the current package.
                new ClassPathResource("fakeManifest.txt", getClass());
        when(mockServletContext.getResourceAsStream(WebUtils.MANIFEST_PATH))
            .thenReturn(manifestResource.getInputStream());

        // Behavior verification
        final Manifest manifest = WebUtils.readWebappManifest(mockServletContext);
        assertEquals("srcalc-test", manifest.getMainAttributes().getValue("Implementation-Title"));
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

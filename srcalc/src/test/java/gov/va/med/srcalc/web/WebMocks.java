package gov.va.med.srcalc.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.core.io.ClassPathResource;

/**
 * Constructs mock web and servlet objects.
 */
public class WebMocks
{
    /**
     * Constructs a mock {@link ServletContext} that will return the fake
     * manifest file from {@link ServletContext#getResourceAsStream(String)}.
     */
    public static ServletContext mockServletContext() throws IOException
    {
        final ServletContext mockServletContext = mock(ServletContext.class);
        final ClassPathResource manifestResource =
                // Find it in the current package.
                new ClassPathResource("fakeManifest.txt", WebMocks.class);
        when(mockServletContext.getResourceAsStream(WebUtils.MANIFEST_PATH))
            .thenReturn(manifestResource.getInputStream());
        return mockServletContext;
    }
    
}

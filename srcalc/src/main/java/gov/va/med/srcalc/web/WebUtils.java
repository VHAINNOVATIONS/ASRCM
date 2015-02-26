package gov.va.med.srcalc.web;

import java.io.*;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

public class WebUtils
{
    /**
     * The standard path to a manifest file.
     */
    public static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";

    /**
     * No construction.
     */
    private WebUtils()
    {
    }

    /**
     * Reads the web application's Manifest.
     * @throws IOException if any I/O error was encountered while reading
     * @throws FileNotFoundException if no manifest file exists
     */
    public static Manifest readWebappManifest(final ServletContext servletContext)
            throws IOException, FileNotFoundException
    {
        try (final InputStream manifestStream = servletContext.getResourceAsStream(MANIFEST_PATH))
        {
            if (manifestStream == null)
            {
                throw new FileNotFoundException("No manifest file " + MANIFEST_PATH);
            }
    
            return new Manifest(manifestStream);
        }
    }
    
}

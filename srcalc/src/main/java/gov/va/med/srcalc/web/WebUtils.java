package gov.va.med.srcalc.web;

import gov.va.med.srcalc.SrcalcInfo;

import java.io.*;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtils
{
    /**
     * The standard path to a manifest file.
     */
    public static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";
    
    /**
     * The default name string to use if the manifest could not be read.
     */
    protected static final String DEFAULT_APP_NAME = "srcalc";
    
    /**
     * The default version string to use if the manifest could not be read.
     */
    protected static final String DEFAULT_APP_VERSION = "unknown (no manifest)";
    
    private static final Logger fLogger = LoggerFactory.getLogger(WebUtils.class);

    /**
     * No construction.
     */
    private WebUtils()
    {
    }

    /**
     * Reads the web application's Manifest.
     * @param servletContext to locate the manifest file
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
    
    /**
     * <p>Reads the web application's basic information from its manifest.</p>
     * 
     * <p>If the manifest could not be read for any reason, this method will
     * just populate the {@link SrcalcInfo} object with some default values.
     * This default behavior handles deployments with no manfiest, such as
     * Tomcat running in Eclipse.</p>
     * 
     * @param servletContext to locate the manifest file
     * @return the loaded {@link SrcalcInfo} instance
     */
    public static SrcalcInfo readSrcalcInfo(final ServletContext servletContext)
    {
        try
        {
            final Manifest appManifest = WebUtils.readWebappManifest(servletContext);
            return SrcalcInfo.fromManifest(appManifest);
        }
        catch (IOException ex)
        {
            fLogger.warn(
                    "Could not read application manifest. Will use a default value for the version.",
                    ex);
            return new SrcalcInfo(DEFAULT_APP_NAME, DEFAULT_APP_VERSION);
        }
    }
    
}

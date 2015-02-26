package gov.va.med.srcalc;

import java.util.jar.Manifest;

/**
 * <p>Contains basic information about the srcalc application, such as the
 * currently-running version.</p>
 * 
 * <p>Right now this class contains <i>only</i> the application version, but it
 * facilitates creating a singleton object (e.g., a Spring bean) containing the
 * version.</p>
 */
public class SrcalcInfo
{
    private final String fVersion;
    
    /**
     * Constructs an instance, explicitly specifying all attributes.
     */
    public SrcalcInfo(final String version)
    {
        fVersion = version;
    }
    
    /**
     * Constructs an instance, reading properties from the given manifest.
     */
    public static SrcalcInfo fromManifest(final Manifest manifest)
    {
        return new SrcalcInfo(
                manifest.getMainAttributes().getValue("Implementation-Version"));
    }

    /**
     * Returns the application version.
     */
    public String getVersion()
    {
        return fVersion;
    }
    
}

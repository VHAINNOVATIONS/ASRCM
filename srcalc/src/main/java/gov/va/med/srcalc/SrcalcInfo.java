package gov.va.med.srcalc;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Contains basic information about the srcalc application, such as the
 * currently-running version.
 */
public class SrcalcInfo
{
    private final String fLongName;
    private final String fVersion;
    
    /**
     * Constructs an instance, explicitly specifying all attributes.
     */
    public SrcalcInfo(final String longName, final String version)
    {
        fLongName = longName;
        fVersion = version;
    }
    
    /**
     * Constructs an instance, reading properties from the given manifest.
     */
    public static SrcalcInfo fromManifest(final Manifest manifest)
    {
        final Attributes mainAttributes = manifest.getMainAttributes();
        return new SrcalcInfo(
                mainAttributes.getValue("Implementation-Title"),
                mainAttributes.getValue("Implementation-Version"));
    }

    /**
     * Returns a possibly-multi-word human-friendly name of the application.
     */
    public String getLongName()
    {
        return fLongName;
    }

    /**
     * Returns the application version.
     */
    public String getVersion()
    {
        return fVersion;
    }
    
}

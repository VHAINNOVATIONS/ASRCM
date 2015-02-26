package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.web.WebUtils;

import java.io.IOException;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Adds common attributes, such as the application version, to the Spring MVC
 * Model.
 */
@ControllerAdvice
public class CommonAttributesAdvice
{
    /**
     * The name of the model attribute which contains the app version.
     * @see #getAppVersion()
     */
    public static final String MODEL_ATTRIBUTE_APP_VERSION = "appVersion";
    
    protected static final String DEFAULT_APP_VERSION = "unknown (no manifest)";
    
    private static final Logger fLogger = LoggerFactory.getLogger(CommonAttributesAdvice.class);
    
    private final String fAppVersion;
    
    /**
     * Constructs an instance, explicitly specifying all attributes.
     */
    public CommonAttributesAdvice(final String appVersion)
    {
        fAppVersion = appVersion;
    }
    
    /**
     * Constructs an instance, reading properties from a Manifest resource
     * in the given {@link ServletContext}.
     */
    public static CommonAttributesAdvice fromServletContext(final ServletContext servletContext)
    {
        try
        {
            final Manifest appManifest = WebUtils.readWebappManifest(servletContext);
            return new CommonAttributesAdvice(appManifest.getMainAttributes().getValue("Implementation-Version"));
        }
        catch (IOException ex)
        {
            fLogger.warn("Could not read application manifest. Will use a default value for the version.", ex);
            return new CommonAttributesAdvice(DEFAULT_APP_VERSION);
        }
    }

    /**
     * Returns the application version (probably for displaying somewhere on the
     * UI).
     */
    @ModelAttribute(MODEL_ATTRIBUTE_APP_VERSION)
    public String getAppVersion()
    {
        return fAppVersion;
    }
    
}

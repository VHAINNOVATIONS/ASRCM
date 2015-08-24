package gov.va.med.srcalc;

/**
 * Indicates that some environment of SRCalc (e.g., a configuration file,
 * application server, etc.) is misconfigured.
 */
public class ConfigurationException extends RuntimeException
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * See {@link RuntimeException#RuntimeException(String)}.
     */
    public ConfigurationException(final String message)
    {
        super(message);
    }
    
    /**
     * See {@link RuntimeException#RuntimeException(String, Throwable)}.
     */
    public ConfigurationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
    
    /**
     * See {@link RuntimeException#RuntimeException(String, Throwable, boolean, boolean)}.
     */
    public ConfigurationException(
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writeableStackTrace)
    {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
    
}

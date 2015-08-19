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
     * @see RuntimeException#RuntimeException(String)
     */
    public ConfigurationException(String message)
    {
        super(message);
    }
    
    /**
     * @see RuntimeException#RuntimeException(String, Throwable)
     */
    public ConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    /**
     * @see RuntimeException#RuntimeException(String, Throwable, boolean, boolean)
     */
    public ConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace)
    {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
    
}

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
    
    public ConfigurationException(String message)
    {
        super(message);
    }
    
    public ConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public ConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace)
    {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
    
}

package gov.va.med.srcalc.service;

/**
 * Indicates that the Service Layer received an invalid identifier (e.g., a
 * specialty name) from the caller.
 */
public class InvalidIdentifierException extends Exception
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    public InvalidIdentifierException(String message)
    {
        super(message);
    }
    
    public InvalidIdentifierException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
}

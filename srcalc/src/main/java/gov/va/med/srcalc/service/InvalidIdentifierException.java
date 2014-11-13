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

    public InvalidIdentifierException(final String message)
    {
        super(message);
    }
    
    public InvalidIdentifierException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
    
}

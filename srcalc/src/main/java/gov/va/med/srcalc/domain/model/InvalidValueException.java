package gov.va.med.srcalc.domain.model;

/**
 * Indicates that a given value is invalid for a given {@link Variable}.
 */
public class InvalidValueException extends Exception
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;
    
    private final String fErrorCode;
    
    /**
     * Like {@link #InvalidValueException(String, String, Throwable)} but with
     * a null cause.
     */
    public InvalidValueException(final String errorCode, final String message)
    {
        super(message);
        
        fErrorCode = errorCode;
    }
    
    /**
     * Constructs an instance.
     * @param errorCode a short, alphanumeric code that may be used for looking
     * up a localized string
     * @param message the standard {@link Throwable} detail message
     * @param cause the standard {@link Throwable} cause
     */
    public InvalidValueException(
            final String errorCode, final String message, final Throwable cause)
    {
        super(message, cause);
        
        fErrorCode = errorCode;
    }

    /**
     * Returns the error code given in the constructor.
     */
    public String getErrorCode()
    {
        return fErrorCode;
    }
    
    /**
     * Returns a message containing everything in {@link Exception#toString()}
     * as well as the error code.
     */
    @Override
    public String toString()
    {
        return String.format("%s (%s)", super.toString(), getErrorCode());
    }
}

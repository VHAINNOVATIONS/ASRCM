package gov.va.med.srcalc.domain.variable;

/**
 * <p>Indicates that a given value was too low for a given {@link Variable}.</p>
 * 
 * <p>The error code will always be {@link #ERROR_CODE_INCLUSIVE} or 
 * {@link #ERROR_CODE_EXCLUSIVE}.</p>
 */
public class ValueTooLowException extends InvalidValueException
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constant error code, "tooLowInclusive".
     */
    public static final String ERROR_CODE_INCLUSIVE = "tooLowInclusive";
    
    /**
     * The constant error code, "tooLowExclusive".
     */
    public static final String ERROR_CODE_EXCLUSIVE = "tooLowExclusive";
    
    /**
     * A constructor that allows inclusive to be specified through the error
     * code.
     * @param message the exception message
     * @param code the spring error code to use
     */
    public ValueTooLowException(final String message, final String code)
    {
    	super(code, message);
    }
    
    /**
     * A constructor that assumes the lower bound is inclusive.
     * @param message the exception message
     */
    public ValueTooLowException(final String message)
    {	
        super(ERROR_CODE_INCLUSIVE, message);
    }
}

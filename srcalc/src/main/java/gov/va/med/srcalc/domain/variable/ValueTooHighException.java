package gov.va.med.srcalc.domain.variable;

/**
 * <p>Indicates that a given value was too high for a given {@link Variable}.</p>
 * 
 * <p>The error code will always be {@link #ERROR_CODE_INCLUSIVE} or 
 * {@link #ERROR_CODE_EXCLUSIVE}.</p>
 */
public class ValueTooHighException extends InvalidValueException
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constant error code, "tooHighInclusive".
     */
    public static final String ERROR_CODE_INCLUSIVE = "tooHighInclusive";
    
    /**
     * The constant error code, "tooHighExclusive".
     */
    public static final String ERROR_CODE_EXCLUSIVE = "tooHighExclusive";
    
    /**
     * A constructor that allows inclusive to be specified through the error
     * code.
     * @param message the exception message
     * @param code the spring error code to use
     */
    public ValueTooHighException(final String code, final String message)
    {
        super(code, message);
    }
    
    /**
     * A constructor that assumes the upper bound is inclusive.
     * @param message the exception message
     */
    public ValueTooHighException(final String message)
    {
        super(ERROR_CODE_INCLUSIVE, message);
    }
}

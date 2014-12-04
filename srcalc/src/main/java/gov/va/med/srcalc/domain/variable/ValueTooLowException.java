package gov.va.med.srcalc.domain.variable;

/**
 * <p>Indicates that a given value was too low for a given {@link Variable}.</p>
 * 
 * <p>The error code will always be {@link #ERROR_CODE}.</p>
 */
public class ValueTooLowException extends InvalidValueException
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constant error code, "tooLow".
     */
    public static final String ERROR_CODE = "tooLow";
    
    public ValueTooLowException(String message)
    {
        super(ERROR_CODE, message);
    }
}

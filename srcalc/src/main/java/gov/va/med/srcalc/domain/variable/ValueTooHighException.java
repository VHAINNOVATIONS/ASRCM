package gov.va.med.srcalc.domain.variable;

/**
 * <p>Indicates that a given value was too high for a given {@link Variable}.</p>
 * 
 * <p>The error code will always be {@link #ERROR_CODE}.</p>
 */
public class ValueTooHighException extends InvalidValueException
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constant error code, "tooHigh".
     */
    public static final String ERROR_CODE = "tooHigh";
    
    public ValueTooHighException(String message)
    {
        super(ERROR_CODE, message);
    }
}

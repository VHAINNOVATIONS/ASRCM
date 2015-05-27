package gov.va.med.srcalc.util;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Utility class offering more convenient methods in the spirit of Spring's
 * {@link ValidationUtils}.
 */
public final class ValidationUtils2
{
    private static final Logger fLogger = LoggerFactory.getLogger(ValidationUtils2.class);
    
    /**
     * Rejects the identified {@link CharSequence} field if it is longer than
     * the given maximum length. If the field is rejected, it will be with the
     * error code {@link ValidationCodes#TOO_LONG}.
     * @param e the Errors instance to register errors on
     * @param field identifies the field. The identified field must be a
     * CharSequence.
     * @param maxLength the maxmimum valid length of the field
     * @return true if the field was rejected, false otherwise
     * @throws ClassCastException if the identified field is not a CharSequence
     */
    public static boolean rejectIfTooLong(
            final Errors e, final String field, final int maxLength)
    {
        fLogger.trace("Validating that {} length is within {}.", field, maxLength);

        // See method contract: the field must be a CharSequence.
        final CharSequence value = (CharSequence)e.getFieldValue(field);
        if (value.length() > maxLength)
        {
            e.rejectValue(
                    field,
                    ValidationCodes.TOO_LONG,
                    new Object[] {maxLength},
                    "too long");
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Rejects the identified {@link CharSequence} field if it does not match
     * the given regular expression. If the field is rejected, it will be with
     * the error code {@link ValidationCodes#INVALID_CONTENTS}.
     * 
     * @param e the Errors instance to register errors on
     * @param field identifies the field. The identified field must be a
     * CharSequence.
     * @param regex the compiled regular expression
     * @param errorArgs the errorArgs to pass to {@link
     * Errors#rejectValue(String, String, Object[], String) rejectValue}
     * @return true if the field was rejected, false otherwise
     * @throws ClassCastException if the identified field is not a CharSequence
     */
    public static boolean rejectIfDoesntMatch(
            final Errors  e, final String field, final Pattern regex, final Object[] errorArgs)
    {
        fLogger.trace("Validating that {} matches {}.", field, regex);

        // See method contract: the field must be a CharSequence.
        final CharSequence value = (CharSequence)e.getFieldValue(field);
        if (!regex.matcher(value).matches())
        {
            e.rejectValue(
                    field,
                    ValidationCodes.INVALID_CONTENTS,
                    errorArgs,
                    "invalid contents");
            return true;
        }
        else
        {
            return false;
        }
    }
    
}

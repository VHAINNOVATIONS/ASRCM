package gov.va.med.srcalc.util;

/**
 * Contains validation error codes used in the application.
 */
public class ValidationCodes
{
    /**
     * Error code used when a required value is not provided.
     */
    public static final String NO_VALUE = "noInput";
    
    /**
     * Error code used when a value must not be provided but was.
     */
    public static final String NOT_APPLICABLE = "notApplicable";
    
    /**
     * Error code used when a value is shorter than the minimum length.
     */
    public static final String TOO_SHORT = "tooShort";

    /**
     * Error code used when a value is longer than the maximum length.
     */
    public static final String TOO_LONG = "tooLong";

    /**
     * Error code used when an invalid value is given for a multi-select option.
     */
    public static final String INVALID_OPTION = "invalidOption";

    /**
     * Error code used when a String value contains invalid characters.
     */
    public static final String INVALID_CONTENTS = "invalidContents";
    
    /**
     * Error code used when a value must be unique but is not.
     */
    public static final String DUPLICATE_VALUE = "duplicateValue";
    
    /**
     * Error code used when a value cannot be converted from a one type (typically a
     * String) to the needed type.
     */
    public static final String TYPE_MISMATCH = "typeMismatch";
    
    /**
     * Error code used when a value must be a specific length but is not.
     */
    public static final String BAD_FIXED_LENGTH = "badFixedLength";

    /**
     * Error code used when a rule's summand expression is not a valid Spring Expression.
     */
    public static final String INVALID_EXPRESSION = "invalidExperession";
    
    /**
     * No construction.
     */
    private ValidationCodes()
    {
    }
}

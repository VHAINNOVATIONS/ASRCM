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
     * No construction.
     */
    private ValidationCodes()
    {
    }
}

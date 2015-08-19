package gov.va.med.srcalc.util;

import java.util.regex.Pattern;

/**
 * A class for storing conditions that pertain to display names and their constraints.
 * Display names have been standardized across the application.
 */
public class DisplayNameConditions
{
    /**
     * The maximum length of a valid display name: {@value}.
     */
    public static final int DISPLAY_NAME_MAX = 80;
    
    /**
     * English description of the valid display name characters for readable
     * error messages.
     * @see #VALID_DISPLAY_NAME_REGEX
     */
    public static final String VALID_DISPLAY_NAME_CHARACTERS =
            "letters, digits, spaces, and ~`!@#$%^&*()-_+=|\\.,<>/?'\":;";
    
    /**
     * <p>A regular expression that defines a valid displayName: {@value}</p>
     * 
     * <p>Note that this expression permits a string of any length. Validation
     * code should check the string length first with a length-specific error
     * message.</p>
     */
    public static final String VALID_DISPLAY_NAME_REGEX = "[\\w ~`!@#$%^&*()-_+=|\\.,<>/?'\":;]*";
    
    /**
     * A pre-compiled version of {@link #VALID_DISPLAY_NAME_REGEX} for efficiency.
     */
    public static final Pattern VALID_DISPLAY_NAME_PATTERN = Pattern.compile(VALID_DISPLAY_NAME_REGEX);
}

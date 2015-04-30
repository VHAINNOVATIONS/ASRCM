package gov.va.med.srcalc.util;

import java.util.regex.Pattern;

/**
 * Utilities to verify method preconditions.
 */
public class Preconditions
{
    /**
     * Not intended to be constructed.
     */
    private Preconditions()
    {
    }

    /**
     * Checks that the given argument is not null and within the provided
     * length.
     * @param s the value to check
     * @param maxLength the maximum length
     * @return the valid value for convenience
     * @throws NullPointerException if the string is null
     * @throws IllegalArgumentException if string is longer than maxLength
     */
    public static String requireWithin(final String s, final int maxLength)
    {
        if (s.length() > maxLength)
        {
            throw new IllegalArgumentException(
                    "The argument must be " + maxLength + " characters or less.");
        }
        
        return s;
    }
    
    /**
     * Checks that the given argument matches the provided regular expression.
     * The regular expression must be compiled to encourage calling code to
     * precompile it.
     * @param arg the value to check
     * @param argName the name of the argument for the error message
     * @param pattern the precompiled regular expression
     * @return the valid value for convenience
     * @throws IllegalArgumentException if the string does not match the regular
     * expression. The message will contain the arg name, the pattern, and the
     * actual value
     */
    public static String requireMatches(
            final String arg, final String argName, final Pattern pattern)
    {
        if (!pattern.matcher(arg).matches())
        {
            throw new IllegalArgumentException(String.format(
                    "%s must match %s (was %s)", argName, pattern, arg));
        }
        
        return arg;
    }
}

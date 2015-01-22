package gov.va.med.srcalc.util;

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
     * @return the valid value
     * @throws NullPointerException if the string is null
     * @throws IllegalArgumentException if string is longer than maxLength
     */
    public static String requireWithin(final String s, final int maxLength)
    {
        if (s.length() > maxLength)
        {
            throw new IllegalArgumentException(
                    "The display name must be " + maxLength + " characters or less.");
        }
        
        return s;
    }
    
}

package gov.va.med.srcalc.util.csv;

import org.apache.commons.csv.CSVRecord;

/**
 * General-purpose utilities for parsing comma-separated value (CSV) files.
 */
public class ParseUtils
{
    private ParseUtils()
    {
        // No construction.
    }

    /**
     * Returns the indexed value for the CSVRecord, as {@link CSVRecord#get(int)}, but
     * returns an empty string for a missing value.
     * @return the value at the specified index, if it exists, or an empty string
     * otherwise
     */
    public static String tryGetValue(final CSVRecord record, final int i)
    {
        return (record.size() > i) ? record.get(i) : "";
    }

    /**
     * Parses a Y/N string into a boolean value.
     * @param s must be "Y" or "N" (case-insensitive)
     * @return true for "Y", false for "N"
     * @throws IllegalArgumentException if s is not "Y" or "N"
     */
    public static boolean parseCsvBoolean(final String s)
    {
        if (s.equalsIgnoreCase("Y"))
        {
            return true;
        }
        else if (s.equalsIgnoreCase("N"))
        {
            return false;
        }
        else
        {
            throw new IllegalArgumentException("The String value must be Y or N.");
        }
    }
    
}

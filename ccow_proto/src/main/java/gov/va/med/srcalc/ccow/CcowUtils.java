package gov.va.med.srcalc.ccow;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

/**
 * CCOW CMA utility methods.
 */
public class CcowUtils
{
    /**
     * No construction.
     */
    private CcowUtils()
    {
    }
    
    /**
     * A {@link GenericType} instance representing a MultivaluedMap&lt;String,
     * String&gt;. This constant is useful because the type is a little
     * unintuitive to construct.
     */
    public static final GenericType<MultivaluedMap<String, String>> MULTI_MAP_TYPE =
            new GenericType<MultivaluedMap<String, String>>() {};
            
    /**
     * Converts a List of Objects into a CMA-style array. The Objects in the
     * list will be converted into Strings using {@link Object#toString()}.
     * @return an array in CMA format
     */
    public static String listToCmaArray(final List<?> items)
    {
        return Joiner.on("|").join(items);
    }
    
    /**
     * Converts a CMA-style array into a List of Strings.
     * @return an immutable list
     */
    public static ImmutableList<String> cmaArrayToList(final String cmaArray)
    {
        // Use Guava's Splitter instead of String.split() due to better
        // interface.
        return ImmutableList.copyOf(Splitter.on('|').split(cmaArray));
    }
}

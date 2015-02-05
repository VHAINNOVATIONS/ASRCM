package gov.va.med.srcalc.ccow;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;

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
}

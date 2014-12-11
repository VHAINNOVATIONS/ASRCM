package gov.va.med.srcalc.util;

import java.util.Collection;
import java.util.List;

/**
 * Utility methods for {@link Collection} and its subtypes.
 */
public class CollectionUtils
{
    /**
     * Prohibit construction.
     */
    private CollectionUtils()
    {
    }

    /**
     * Truncates the given list to have the given maximum size.
     * @param full the full ist to truncate
     * @param maxSize the maximum size of the returned view
     * @return a view of the full list as with {@link List#subList(int, int)}.
     */
    public static <E> List<E> truncateList(final List<E> full, int maxSize)
    {
        final int numToDisplay = Math.min(full.size(), maxSize);
        return full.subList(0, numToDisplay);
    }
    
}

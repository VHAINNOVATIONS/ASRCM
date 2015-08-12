package gov.va.med.srcalc.util;

import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

/**
 * <p>Encapsulates the results of a search, including the actual items found and whether
 * the list of items was truncated due to a maximum. Immutable.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 * 
 * @param <T> the type of objects found
 */
public final class SearchResults<T>
{
    private final ImmutableList<T> fFoundItems;
    private final boolean fTruncated;
    
    /**
     * Constructs an instance with the given properties.
     * @param foundItems See {@link #getFoundItems()}. Defensively-copied.
     * @param truncated See {@link #isTruncated()}.
     */
    public SearchResults(
            final List<T> foundItems, final boolean truncated)
    {
        fFoundItems = ImmutableList.copyOf(foundItems);
        fTruncated = truncated;
    }
    
    /**
     * Constructs an instance by truncating the given list to the given maximum. We
     * provide this method because it is common for search code to detect exceeding a
     * maximum by searching for max + 1 results and then truncating the list.
     * @return a SearchResults containing the first {@code max} items in {@code foundItems}
     */
    public static <T> SearchResults<T> fromList(final List<T> foundItems, final int max)
    {
        if (foundItems.size() > max)
        {
            return new SearchResults<>(foundItems.subList(0, max), true);
        }
        else
        {
            return new SearchResults<>(foundItems, false);
        }
    }

    /**
     * Returns the items found by the search. The method creating this object should
     * specify the order of these items, if any.
     */
    public ImmutableList<T> getFoundItems()
    {
        return fFoundItems;
    }

    /**
     * Returns true if the list of items is truncated due to reaching a maximum; false
     * otherwise.
     */
    public boolean isTruncated()
    {
        return fTruncated;
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("truncated", fTruncated)
                // Don't include the whole list as it may have thousands of items.
                .add("#ofItems", fFoundItems.size())
                .toString();
    }
    
    /**
     * Returns true if the given object is also an instance SearchResults with the same
     * items found and trunacated status. Note that two empty SearchResults for different
     * types of items are considered equal due to type erasure.
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof SearchResults)
        {
            // We neither know nor consider the type: see method doc.
            final SearchResults<?> other = (SearchResults<?>)obj;
            
            return (this.fTruncated == other.fTruncated) &&
                    Objects.equals(this.fFoundItems, other.fFoundItems);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(fFoundItems, fTruncated);
    }
}

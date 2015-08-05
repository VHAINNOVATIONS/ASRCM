package gov.va.med.srcalc.util;

import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Tests the {@link SearchResults} class.
 */
public class SearchResultsTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(SearchResults.class).verify();
    }
    
    @Test
    public final void testFromList()
    {
        final ImmutableList<Integer> values = ImmutableList.of(1, 2, 3, 4, 5, 6);
        assertEquals(
                new SearchResults<>(ImmutableList.of(1, 2, 3, 4, 5), true),
                SearchResults.fromList(values, 5));
        assertEquals(
                new SearchResults<>(values, false),
                SearchResults.fromList(values, 100));
    }
    
}

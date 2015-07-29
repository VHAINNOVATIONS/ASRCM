package gov.va.med.srcalc.util;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * Tests the {@link SearchResults} class.
 */
public class SearchResultsTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(SearchResults.class);
    }
    
}

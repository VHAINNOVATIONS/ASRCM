package gov.va.med.srcalc.db;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

/**
 * Unit tests for {@link HistoricalSearchParameters}.
 */
public class HistoricalSearchParametersTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(HistoricalSearchParameters.class)
            // Mutability warning is documented on equals().
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
    
    @Test
    public final void testNoFilters()
    {
        /* Setup */
        final HistoricalSearchParameters params = new HistoricalSearchParameters();
        params.setMinDate(null);
        params.setMaxDate(null);
        
        /* Behavior & Verification */
        assertSame(null, params.getMinDate());
        assertSame(null, params.getMaxDate());
        
        // We don't specify the format of toString() at all, but at least make sure it
        // is non-empty.
        assertThat(params.toString(), not(isEmptyOrNullString()));
    }
    
    // Note: we test the actual search in ResultsDaoIT.
}

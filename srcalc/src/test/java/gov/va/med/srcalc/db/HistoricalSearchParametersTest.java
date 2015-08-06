package gov.va.med.srcalc.db;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

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
        assertEquals(ImmutableMap.<String, Object>of(), params.getAppliedParameters());
        
        // We don't specify the format of toString() at all, but at least make sure it
        // is non-empty.
        assertThat(params.toString(), not(isEmptyOrNullString()));
    }
    
    @Test
    public final void testAllFilters()
    {
        final LocalDate minDate = new LocalDate(2014, 2, 4);
        final LocalDate maxDate = new LocalDate(2015, 1, 31);
        /* Setup */
        final HistoricalSearchParameters params = new HistoricalSearchParameters();
        params.setMinDate(minDate);
        params.setMaxDate(maxDate);
        
        /* Behavior & Verification */
        assertSame(minDate, params.getMinDate());
        assertSame(maxDate, params.getMaxDate());
        assertEquals(
                ImmutableMap.of(
                        HistoricalSearchParameters.PARAM_MIN_DATE, minDate,
                        HistoricalSearchParameters.PARAM_MAX_DATE, maxDate),
                params.getAppliedParameters());
    }
    
    // Note: we test the actual search in ResultsDaoIT.
}

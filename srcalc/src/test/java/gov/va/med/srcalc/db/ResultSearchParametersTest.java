package gov.va.med.srcalc.db;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Unit tests for {@link ResultSearchParameters}.
 */
public class ResultSearchParametersTest
{
    @Test
    public final void testEqualsObject()
    {
        EqualsVerifier.forClass(ResultSearchParameters.class)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
    
    @Test
    public final void testNoFilters()
    {
        /* Setup */
        final ResultSearchParameters params = new ResultSearchParameters();
        params.setMinDate(null);
        params.setMaxDate(null);
        params.setStationNumber("");
        params.setSpecialtyNames(ImmutableSet.<String>of());
        params.setCptCode("");
        
        /* Behavior & Verification */
        assertSame(null, params.getMinDate());
        assertSame(null, params.getMaxDate());
        assertSame(null, params.getStationNumber());
        assertEquals(ImmutableSet.of(), params.getSpecialtyNames());
        assertSame(null, params.getCptCode());
        
        // We don't specify the format of toString() at all, but at least make sure it
        // is non-empty.
        assertThat(params.toString(), not(isEmptyOrNullString()));
    }
    
    // Note: we test the actual search in ResultsDaoIT.
    
}

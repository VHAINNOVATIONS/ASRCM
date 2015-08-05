package gov.va.med.srcalc.web.view.admin;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * Unit tests for the {@link ModelTermSummary} class.
 */
public class ModelTermSummaryTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(ModelTermSummary.class).verify();
    }
    
    @Test
    public final void testBasic()
    {
        final String identString = "identString";
        final String termType = "termType";
        final float coeff = 0.023f;
        final ModelTermSummary summary = new ModelTermSummary(identString, termType, coeff);
        
        assertEquals(identString, summary.getIdentificationString());
        assertEquals(termType, summary.getTermType());
        assertEquals(coeff, summary.getCoefficient(), 0.0f);
        
        // We don't specify the format of toString() at all, but at least make sure it
        // is non-empty.
        assertThat(summary.toString(), not(isEmptyOrNullString()));
    }
}

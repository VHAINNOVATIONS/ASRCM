package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.BooleanVariable;
import gov.va.med.srcalc.util.CollectionUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class BooleanTermTest
{
    @Test
    public final void testBasic()
    {
        final float coeff = -1.7f;
        final BooleanVariable var = SampleObjects.dnrVariable();
        final BooleanTerm term = new BooleanTerm(var, coeff);
        
        assertEquals(coeff, term.getCoefficient(), 0.0f);
        assertSame(var, term.getVariable());
        assertEquals(CollectionUtils.hashSet(var), term.getRequiredVariables());
        assertThat(term.toString(), allOf(
                containsString(Float.toString(coeff)), containsString("DNR")));
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(BooleanTerm.class)
            // Public interface disallows null.
            .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS)
            .verify();
    }
    
}

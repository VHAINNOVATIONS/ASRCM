package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

/**
 * Tests the {@link ConstantTerm} class.
 */
public class ConstantTermTest
{
    @Test
    public final void testBasic()
    {
        final float coeff = 35.7f;
        final ConstantTerm term = new ConstantTerm(coeff);
        
        assertEquals(coeff, term.getCoefficient(), 0.0f);
        assertEquals(0, term.getRequiredVariables().size());
        assertThat(term.toString(), containsString(Float.toString(coeff)));
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(ConstantTerm.class)
            // Public interface disallows null.
            .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS)
            .verify();
    }
    
}

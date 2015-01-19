package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.DiscreteVariable;
import gov.va.med.srcalc.domain.variable.MultiSelectOption;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class DiscreteTermTest
{
    @Test
    public final void testBasic()
    {
        final float coeff = 2.25f;
        final DiscreteVariable var = SampleObjects.wbcVariable();
        final MultiSelectOption option = var.getOptions().get(1);
        final DiscreteTerm term = new DiscreteTerm(var, 1, coeff);
        
        assertEquals(coeff, term.getCoefficient(), 0.0f);
        assertSame(var, term.getVariable());
        assertEquals(option, term.getOption());
        assertThat(term.toString(), allOf(
                containsString(Float.toString(coeff)),
                containsString(option.getValue()),
                containsString(var.toString())));
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(DiscreteTerm.class)
            // Public interface disallows null.
            .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS)
            .verify();
    }
}

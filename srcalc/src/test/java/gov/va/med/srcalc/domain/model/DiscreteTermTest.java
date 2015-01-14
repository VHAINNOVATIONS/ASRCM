package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.DiscreteVariable;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class DiscreteTermTest
{
    @Test
    public final void testBasic()
    {
        final DiscreteVariable var = SampleObjects.wbcVariable();
        final DiscreteTerm term = new DiscreteTerm(var, 1, 2.25f);
        
        assertEquals(2.25f, term.getCoefficient(), 0.0f);
        assertSame(var, term.getVariable());
        assertEquals(var.getOptions().get(1), term.getOption());
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

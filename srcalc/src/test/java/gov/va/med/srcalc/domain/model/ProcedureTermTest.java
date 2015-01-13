package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.ProcedureVariable;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class ProcedureTermTest
{
    @Test
    public final void testBasic()
    {
        final ProcedureVariable var = SampleObjects.sampleProcedureVariable();
        final ProcedureTerm term = new ProcedureTerm(var, 0.1f);
        
        assertEquals(0.1f, term.getCoefficient(), 0.0f);
        assertSame(var, term.getVariable());
        assertTrue(term.getRequiredVariables().contains(var));
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(ProcedureTerm.class)
            // Public interface disallows null.
            .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS)
            .verify();
    }
    
}

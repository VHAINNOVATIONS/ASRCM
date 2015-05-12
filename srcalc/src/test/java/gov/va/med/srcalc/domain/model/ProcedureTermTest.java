package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class ProcedureTermTest
{
    @Test
    public final void testBasic()
    {
        final float coeff = 0.1f;
        final ProcedureVariable var = SampleModels.procedureVariable();
        final ProcedureTerm term = new ProcedureTerm(var, coeff);
        
        assertEquals(coeff, term.getCoefficient(), 0.0f);
        assertSame(var, term.getVariable());
        assertTrue(term.getRequiredVariables().contains(var));
        assertThat(term.toString(), allOf(
                containsString(Float.toString(coeff)),
                containsString(var.toString())));
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

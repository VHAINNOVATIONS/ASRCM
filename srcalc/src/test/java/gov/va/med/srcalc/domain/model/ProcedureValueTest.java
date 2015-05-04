package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProcedureValueTest
{
    @Test
    public final void testBasic()
    {
        final ProcedureVariable var = SampleModels.procedureVariable();
        final Procedure sel = var.getProcedures().get(1);
        final ProcedureValue val = new ProcedureValue(var, sel);
        // getVariable()
        assertSame(var, val.getVariable());
        // toString()
        assertEquals(
                "Procedure = " + sel.toString(), val.toString());
        // getValue() - no need to test the actual String, that's covered by
        // ProcedureTest.
        assertEquals(sel.getLongString(), val.getDisplayString());
    }
    
}

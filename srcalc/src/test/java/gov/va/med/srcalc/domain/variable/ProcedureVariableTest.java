package gov.va.med.srcalc.domain.variable;

import org.junit.Test;

public class ProcedureVariableTest
{
    @Test(expected = IllegalStateException.class)
    public final void testGetProceduresUnset()
    {
        new ProcedureVariable().getProcedures();
    }
    
    @Test(expected = IllegalStateException.class)
    public final void testGetProcedureMapUnset()
    {
        new ProcedureVariable().getProcedureMap();
    }
    
}

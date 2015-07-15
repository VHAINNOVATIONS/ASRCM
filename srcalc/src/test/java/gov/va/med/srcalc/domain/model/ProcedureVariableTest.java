package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.model.ProcedureVariable;

import org.junit.Test;

/**
 * Tests the {@link ProcedureVariable} class.
 */
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

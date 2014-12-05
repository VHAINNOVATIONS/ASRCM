package gov.va.med.srcalc.domain.variable;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;

import org.junit.Test;

public class MultiSelectValueTest
{
    @Test
    public final void testBasic()
    {
        final MultiSelectVariable var = SampleObjects.functionalStatusVariable();
        final MultiSelectValue val = new MultiSelectValue(var, var.getOptions().get(1));
        // getVariable()
        assertSame(var, val.getVariable());
        // toString()
        assertEquals("Functional Status = Partially dependent", val.toString());
    }
    
}

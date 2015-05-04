package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.MultiSelectValue;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;

import org.junit.Test;

public class MultiSelectValueTest
{
    @Test
    public final void testBasic()
    {
        final MultiSelectVariable var = SampleModels.functionalStatusVariable();
        final MultiSelectValue val = new MultiSelectValue(var, var.getOptions().get(1));
        // getVariable()
        assertSame(var, val.getVariable());
        // toString()
        assertEquals("Functional Status = Partially dependent", val.toString());
        // getDisplayString()
        assertEquals("Partially dependent", val.getDisplayString());
    }
    
}

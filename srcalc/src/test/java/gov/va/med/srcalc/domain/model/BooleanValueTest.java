package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.model.BooleanValue;
import gov.va.med.srcalc.domain.model.BooleanVariable;

import org.junit.Test;

public class BooleanValueTest
{
    @Test
    public final void testBasicTrue()
    {
        final BooleanVariable var = SampleObjects.dnrVariable();
        final BooleanValue val = new BooleanValue(var, true);
        // getVariable()
        assertSame(var, val.getVariable());
        // toString()
        assertEquals("DNR = true", val.toString());
        // getDisplayString()
        assertEquals("Yes", val.getDisplayString());
    }

    @Test
    public final void testBasicFalse()
    {
        final BooleanVariable var = SampleObjects.dnrVariable();
        final BooleanValue val = new BooleanValue(var, false);
        // getVariable()
        assertSame(var, val.getVariable());
        // toString()
        assertEquals("DNR = false", val.toString());
        // getDisplayString()
        assertEquals("No", val.getDisplayString());
    }
    
}

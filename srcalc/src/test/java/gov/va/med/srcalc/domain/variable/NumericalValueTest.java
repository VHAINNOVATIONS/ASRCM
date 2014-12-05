package gov.va.med.srcalc.domain.variable;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;

import org.junit.Test;

public class NumericalValueTest
{
    @Test
    public final void testBasic() throws Exception
    {
        final NumericalVariable var = SampleObjects.sampleAgeVariable();
        final NumericalValue val = new NumericalValue(var, 1.2f);
        // getVariable()
        assertSame(var, val.getVariable());
        // toString()
        assertEquals("Age = 1.2", val.toString());
    }
}

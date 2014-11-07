package gov.va.med.srcalc.domain.variable;

import gov.va.med.srcalc.domain.variable.MultiSelectOption;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultiSelectOptionTest
{
    @Test
    public final void testToString()
    {
        assertEquals("option name", new MultiSelectOption("option name").toString());
    }
    
}

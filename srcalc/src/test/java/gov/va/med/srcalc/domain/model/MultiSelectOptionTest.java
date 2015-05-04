package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.model.MultiSelectOption;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import static org.junit.Assert.*;

public class MultiSelectOptionTest
{
    @Test
    public final void testToString()
    {
        assertEquals("option name", new MultiSelectOption("option name").toString());
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(MultiSelectOption.class)
            // The class presents an immutable interface.
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }
    
}

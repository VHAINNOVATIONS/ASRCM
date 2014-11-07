package gov.va.med.srcalc.domain;

import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class SpecialtyTest
{
    @Test
    public final void testToString()
    {
        final String name = "Jack-of-all-trades";
        assertEquals(name, new Specialty(0, 59, name).toString());
    }
    
    @Test
    public final void testEqualsObject()
    {
        EqualsVerifier.forClass(Specialty.class).verify();
    }
    
}

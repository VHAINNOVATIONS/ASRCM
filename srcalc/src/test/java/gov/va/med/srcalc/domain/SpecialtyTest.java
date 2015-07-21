package gov.va.med.srcalc.domain;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.test.util.TestHelpers;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class SpecialtyTest
{
    @Test
    public final void testToString()
    {
        final String name = "Jack-of-all-trades";
        assertEquals(name, new Specialty(59, name).toString());
    }
    
    @Test
    public final void testEqualsObject()
    {
        EqualsVerifier.forClass(Specialty.class)
            .suppress(Warning.NULL_FIELDS)  // variables may not be null
            .verify();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testNameTooLongConstruction()
    {
        new Specialty(
                59,
                TestHelpers.stringOfLength(Specialty.SPECIALTY_NAME_MAX + 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testNameTooLongSetter()
    {
        final Specialty s = new Specialty(59, "SpecialtyName");
        s.setName(TestHelpers.stringOfLength(Specialty.SPECIALTY_NAME_MAX + 1));
    }
}

package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.test.util.TestHelpers;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the {@link MultiSelectOption} class.
 */
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
    
    @Test(expected = IllegalArgumentException.class)
    public final void testValueEmpty()
    {
        new MultiSelectOption("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testValueTooLong()
    {
        new MultiSelectOption(
                TestHelpers.stringOfLength(MultiSelectOption.VALUE_MAX + 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testValueInvalidCharacters()
    {
        new MultiSelectOption("foo\t");
    }
    
}

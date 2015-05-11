package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.test.util.TestHelpers;

import org.junit.Test;

import com.google.common.base.Optional;

public class AbstractVariableTest
{
    @Test
    public final void testSetDisplayNameValid()
    {
        // Split it up or else it's greater than the max length.
        final String validLetters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final String validSymbols = " ~`!@#$%^&*()-_+=|\\.,<>/?'\":;";

        final AbstractVariable var = SampleModels.ageVariable();
        var.setDisplayName(validLetters);
        assertEquals(validLetters, var.getDisplayName());
        var.setDisplayName(validSymbols);
        assertEquals(validSymbols, var.getDisplayName());
    }

    @Test(expected = NullPointerException.class)
    public final void testSetDisplayNameNull()
    {
        final AbstractVariable var = SampleModels.ageVariable();
        var.setDisplayName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetDisplayNameTooLong()
    {
        final AbstractVariable var = SampleModels.ageVariable();
        var.setDisplayName(TestHelpers.stringOfLength(Variable.DISPLAY_NAME_MAX + 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testSetDisplayNameInvalidChars()
    {
        final AbstractVariable var = SampleModels.ageVariable();
        var.setDisplayName("\t");  // tab character is not valid
    }

    @Test
    public final void testSetKeyValid()
    {
        final String maxString = TestHelpers.stringOfLength(Variable.KEY_MAX);
        final AbstractVariable var = SampleModels.ageVariable();

        var.setKey(maxString);
        assertEquals(maxString, var.getKey());
    }
    
    @Test(expected = NullPointerException.class)
    public final void testSetKeyNull()
    {
        final AbstractVariable var = SampleModels.ageVariable();
        var.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetKeyTooLong()
    {
        final AbstractVariable var = SampleModels.ageVariable();
        var.setKey(TestHelpers.stringOfLength(Variable.KEY_MAX + 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetKeyInvalidChars()
    {
        final AbstractVariable var = SampleModels.ageVariable();
        var.setKey("PTA/PCI Procedure");  // slashes are not permitted
    }
    
    @Test
    public final void testSetHelpTextValid()
    {
        final String maxString = TestHelpers.stringOfLength(Variable.HELP_TEXT_MAX);
        final AbstractVariable var = SampleModels.ageVariable();
        // Try the max length
        var.setHelpText(Optional.of(maxString));
        assertEquals(maxString, var.getHelpText().get());
        assertEquals(maxString, var.getHelpTextString());
        // Also try an absent optional, which is valid
        var.setHelpText(Optional.<String>absent());
        assertFalse(var.getHelpText().isPresent());
        assertNull(var.getHelpTextString());
    }
    
    @Test(expected = NullPointerException.class)
    public final void testSetHelpTextNull()
    {
        final AbstractVariable var = SampleModels.ageVariable();
        var.setHelpText(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testSetHelpTextEmpty()
    {
        final AbstractVariable var = SampleModels.ageVariable();
        var.setHelpText(Optional.of(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testSetHelpTextTooLong()
    {
        final AbstractVariable var = SampleModels.ageVariable();
        var.setHelpText(Optional.of(
                TestHelpers.stringOfLength(Variable.HELP_TEXT_MAX + 1)));
    }
    
}

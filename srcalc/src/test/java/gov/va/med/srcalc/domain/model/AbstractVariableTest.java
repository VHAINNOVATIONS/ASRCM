package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.SampleModels;

import org.junit.Test;

import com.google.common.base.Optional;

public class AbstractVariableTest
{
    /**
     * A 40-character string.
     */
    public static final String FORTY_CHARS =
            "0123456789012345678901234567890123456789";

    /**
     * A 41-character string.
     */
    public static final String FORTY_ONE_CHARS =
            "0123456789012345678901234567890123456789X";

    /**
     * An 81-characeter string.
     */
    public static final String EIGHTY_ONE_CHARS =
            "01234567890123456789012345678901234567890123456789012345678901234567890123456789X";
    
    /**
     * A 4000-character string.
     */
    public static final String FOUR_THOUSAND_CHARS =
            "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "8901234567890123456789012345678901234567890123456789012345678901"
            + "2345678901234567890123456789012345678901234567890123456789012345"
            + "6789012345678901234567890123456789012345678901234567890123456789"
            + "0123456789012345678901234567890123456789012345678901234567890123"
            + "4567890123456789012345678901234567890123456789012345678901234567"
            + "89012345678901234567890123456789";

    public static final String FOUR_THOUSAND_ONE_CHARS = FOUR_THOUSAND_CHARS + "X";
    
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
        var.setDisplayName(EIGHTY_ONE_CHARS);
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
        final AbstractVariable var = SampleModels.ageVariable();
        var.setKey(FORTY_CHARS);
        assertEquals(FORTY_CHARS, var.getKey());
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
        var.setKey(FORTY_ONE_CHARS);
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
        final AbstractVariable var = SampleModels.ageVariable();
        // Try the max length
        var.setHelpText(Optional.of(FOUR_THOUSAND_CHARS));
        assertEquals(FOUR_THOUSAND_CHARS, var.getHelpText().get());
        assertEquals(FOUR_THOUSAND_CHARS, var.getHelpTextString());
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
        var.setHelpText(Optional.of(FOUR_THOUSAND_ONE_CHARS));
    }
    
}

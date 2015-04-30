package gov.va.med.srcalc.domain.variable;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;

import org.junit.Test;

public class AbstractVariableTest
{
    /**
     * A 40-character string.
     */
    private static final String FORTY_CHARS =
            "0123456789012345678901234567890123456789";

    /**
     * A 41-character string.
     */
    private static final String FORTY_ONE_CHARS =
            "0123456789012345678901234567890123456789X";

    /**
     * An 81-characeter string.
     */
    private static final String EIGHTY_ONE_CHARS =
            "01234567890123456789012345678901234567890123456789012345678901234567890123456789X";

    @Test(expected = NullPointerException.class)
    public final void testSetDisplayNameNull()
    {
        final AbstractVariable var = SampleObjects.sampleAgeVariable();
        var.setDisplayName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetDisplayNameTooLong()
    {
        final AbstractVariable var = SampleObjects.sampleAgeVariable();
        var.setDisplayName(EIGHTY_ONE_CHARS);
    }
    
    @Test(expected = NullPointerException.class)
    public final void testSetKeyNull()
    {
        final AbstractVariable var = SampleObjects.sampleAgeVariable();
        var.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetKeyTooLong()
    {
        final AbstractVariable var = SampleObjects.sampleAgeVariable();
        var.setKey(FORTY_ONE_CHARS);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetKeyInvalidChars()
    {
        final AbstractVariable var = SampleObjects.sampleAgeVariable();
        var.setKey("PTA/PCI Procedure");
    }

    @Test
    public final void testSetKeyValid()
    {
        final AbstractVariable var = SampleObjects.sampleAgeVariable();
        var.setKey(FORTY_CHARS);
        assertEquals(FORTY_CHARS, var.getKey());
    }
    
}

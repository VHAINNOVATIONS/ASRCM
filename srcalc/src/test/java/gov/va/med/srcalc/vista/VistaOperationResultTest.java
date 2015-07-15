package gov.va.med.srcalc.vista;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the {@link VistaOperationResult} class.
 */
public class VistaOperationResultTest
{
    @Test
    public final void testFromStringValid()
    {
        // Test multiple carets, which is supported for flexibility.
        final String sourceString = "1^Progress note was created^and signed successfully.";
        final VistaOperationResult result = VistaOperationResult.fromString(sourceString);
        
        assertEquals("1", result.getCode());
        assertEquals(
                "Progress note was created^and signed successfully.",
                result.getMessage());
        assertEquals(sourceString, result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testFromStringInvalid()
    {
        VistaOperationResult.fromString("Foobar!");
    }
    
}

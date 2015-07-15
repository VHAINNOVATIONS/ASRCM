package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.InvalidValueException;

import org.junit.Test;

/**
 * Tests the {@link InvalidValueException} class.
 */
public class InvalidValueExceptionTest
{
    @Test
    public final void testToString()
    {
        final String code = "tooLow";
        final String message = "Number was too low";
        final Exception ex = new InvalidValueException(code, message);
        
        // See InvalidValueException.toString() Javadocs: we don't check the
        // full format, just that it contains the error message and code.
        assertTrue("toString should contain the error message",
                ex.toString().contains(message));
        assertTrue("toString should contain the error code",
                ex.toString().contains(code));
    }
    
}

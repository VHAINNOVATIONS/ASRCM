package gov.va.med.srcalc.util;

import static org.hamcrest.CoreMatchers.containsString;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PreconditionsTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public final void testRequireWithinTooLong()
    {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(containsString("2 characters"));
        Preconditions.requireWithin("aaa", 2);
    }
    
}

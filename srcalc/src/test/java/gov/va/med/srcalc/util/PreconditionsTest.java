package gov.va.med.srcalc.util;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests the {@link Preconditions} class.
 */
public class PreconditionsTest
{
    @Rule
    public ExpectedException fExpectedException = ExpectedException.none();

    @Test
    public final void testRequireWithinTooLong()
    {
        fExpectedException.expect(IllegalArgumentException.class);
        fExpectedException.expectMessage(containsString("2 characters"));
        Preconditions.requireWithin("aaa", 0, 2);
    }
    
    @Test
    public final void testRequireWithinTooShort()
    {
        fExpectedException.expect(IllegalArgumentException.class);
        fExpectedException.expectMessage(containsString("3 characters"));
        Preconditions.requireWithin("bb", 3, 40);
    }
    
    @Test
    public final void testRequireMatchesInvalid()
    {
        fExpectedException.expect(IllegalArgumentException.class);
        fExpectedException.expectMessage(allOf(
                containsString("aaa"), containsString("bbb"), containsString("strvalue")));
        
        Preconditions.requireMatches("aaa", "strvalue", Pattern.compile("bbb"));
    }
    
    @Test
    public final void testRequireMatchesValid()
    {
        Preconditions.requireMatches("bbb", "strvalue", Pattern.compile("bbb"));
    }
    
}

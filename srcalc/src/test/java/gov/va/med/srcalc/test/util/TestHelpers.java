package gov.va.med.srcalc.test.util;

import static org.junit.Assert.*;

import java.util.*;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Helper methods useful in automated tests.
 */
public class TestHelpers
{
    /**
     * Verifies the contract of {@link Comparable#compareTo(Object)} given three
     * objects of the same type.
     * @param lesser <code>lesser</code> &lt; <code>middle</code> &lt; <code>greater</code>
     * @param middle see above
     * @param greater see above
     */
    public static <T extends Comparable<T>> void verifyCompareToContract(
            T lesser, T middle, T greater)
    {
        assertEquals("lesser should equal itself", 0, lesser.compareTo(lesser));
        assertTrue("lesser should be less than middle", lesser.compareTo(middle) < 0);
        assertTrue("lesser should be less than greater", lesser.compareTo(greater) < 0);

        assertEquals("middle should equal itself", 0, middle.compareTo(middle));
        assertTrue("middle should be greater than lesser", middle.compareTo(lesser) > 0);
        assertTrue("middle should be less than greater", middle.compareTo(greater) < 0);

        assertEquals("greater should equal itself", 0, greater.compareTo(greater));
        assertTrue("greater should be greater than lesser", greater.compareTo(lesser) > 0);
        assertTrue("greater should be greater than middle", greater.compareTo(middle) > 0);
        
    }
    
    /**
     * Verifies the contract of {@link Comparator#compare(Object, Object)} given three
     * objects of the same type.
     * @param comparator the comparator under test
     * @param lesser <code>lesser</code> &lt; <code>middle</code> &lt; <code>greater</code>
     * @param middle see above
     * @param greater see above
     */
    public static <T> void verifyComparatorContract(
            final Comparator<T> comparator,
            final T lesser, final T middle, final T greater)
    {
        assertEquals("lesser should equal itself",
                0, comparator.compare(lesser, lesser));
        assertTrue("lesser should be less than middle",
                comparator.compare(lesser, middle) < 0);
        assertTrue("lesser should be less than greater",
                comparator.compare(lesser, middle) < 0);

        assertEquals("middle should equal itself",
                0, comparator.compare(middle, middle));
        assertTrue("middle should be greater than lesser",
                comparator.compare(middle, lesser) > 0);
        assertTrue("middle should be less than greater",
                comparator.compare(middle, greater) < 0);

        assertEquals("greater should equal itself",
                0, comparator.compare(greater, greater));
        assertTrue("greater should be greater than lesser",
                comparator.compare(greater, lesser) > 0);
        assertTrue("greater should be greater than middle",
                comparator.compare(greater, middle) > 0);
        
    }
    
    /**
     * Returns a String with exactly the given length. Its contents are
     * otherwise arbitrary
     * @param length the length of the returned string
     */
    public static String stringOfLength(final int length)
    {
        return Strings.padEnd("", length, 'X');
    }

    /**
     * Translates the given FieldErrors into a Set of SimpleFieldErrors.
     */
    public static Set<SimpleFieldError> toSimpleErrors(final Collection<FieldError> errors)
    {
        final HashSet<SimpleFieldError> simpleErrors = new HashSet<>(errors.size());
        for (final FieldError error: errors)
        {
            simpleErrors.add(SimpleFieldError.fromFieldError(error));
        }
        return simpleErrors;
    }

    /**
     * Asserts that the given Errors object has only a specific set of errors. Will throw
     * an {@link AssertionError} if the Errors object does not contain the expected errors
     * or contains other errors.
     * @param errorsObject the Errors object to check
     * @param expectedErrors the expected errors
     */
    public static void assertOnlyTheseErrors(
            final Errors errorsObject,
            final SimpleFieldError... expectedErrors)
    {
        assertEquals(ImmutableList.of(), errorsObject.getGlobalErrors());
        assertEquals(
                ImmutableSet.copyOf(expectedErrors),
                toSimpleErrors(errorsObject.getFieldErrors()));
    }
}

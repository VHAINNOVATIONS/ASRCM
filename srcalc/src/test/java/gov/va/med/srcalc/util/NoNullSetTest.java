package gov.va.med.srcalc.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class NoNullSetTest
{
    @Test
    public final void testBasic()
    {
        final HashSet<Object> origSet = new HashSet<>();
        origSet.add("hi");
        origSet.add("abc123");
        final NoNullSet<Object> noNullSet = NoNullSet.fromSet(origSet);
        
        // Verify equality with the original set.
        assertEquals(origSet, noNullSet);
        assertEquals(origSet.hashCode(), noNullSet.hashCode());
        
        // Verify other operations - basically verifying the Set contract.
        assertFalse(noNullSet.isEmpty());
        assertTrue(noNullSet.toString().contains("abc123"));
        assertTrue(noNullSet.containsAll(Arrays.asList("abc123", "hi")));
        noNullSet.remove("hi");
        assertFalse(noNullSet.contains("hi"));
        noNullSet.clear();
        assertTrue(noNullSet.isEmpty());
        assertTrue(noNullSet.addAll(Arrays.asList("foo", "bar", "baz")));
        assertTrue(noNullSet.removeAll(Arrays.asList("bar", "baz")));
        assertFalse(noNullSet.contains("baz"));
        assertTrue(noNullSet.add("baz"));
        assertTrue(noNullSet.retainAll(Arrays.asList("bar", "baz")));
        // Set should now just contain baz.
        assertEquals("baz", noNullSet.toArray()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testFromSetNoNull()
    {
        final HashSet<Object> containsNull = new HashSet<>();
        containsNull.add("hi");
        containsNull.add(null);
        NoNullSet.fromSet(containsNull);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testAdd()
    {
        final NoNullSet<Object> noNullSet = NoNullSet.fromSet(new HashSet<Object>());
        noNullSet.add(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testAddAll()
    {
        final NoNullSet<Object> noNullSet = NoNullSet.fromSet(new HashSet<Object>());
        final HashSet<Object> containsNull = new HashSet<>();
        containsNull.add("hi");
        containsNull.add(null);
        noNullSet.addAll(containsNull);
    }
    
}

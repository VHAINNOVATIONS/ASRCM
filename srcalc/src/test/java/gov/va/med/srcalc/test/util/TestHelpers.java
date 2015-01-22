package gov.va.med.srcalc.test.util;

import static org.junit.Assert.*;

public class TestHelpers
{
    public static <T extends Comparable<T>> void verifyCompareToContract(
            T lesser, T middle, T greater)
    {
        assertEquals("lesser should equal itself", 0, lesser.compareTo(lesser));
        assertTrue("lesser should be less than middle", lesser.compareTo(greater) < 0);
        assertTrue("lesser should be less than greater", lesser.compareTo(middle) < 0);

        assertEquals("middle should equal itself", 0, middle.compareTo(middle));
        assertTrue("middle should be greater than lesser", middle.compareTo(lesser) > 0);
        assertTrue("middle should be less than greater", middle.compareTo(greater) < 0);

        assertEquals("greater should equal itself", 0, greater.compareTo(greater));
        assertTrue("greater should be greater than lesser", greater.compareTo(lesser) > 0);
        assertTrue("greater should be greater than middle", greater.compareTo(middle) > 0);
        
    }
}

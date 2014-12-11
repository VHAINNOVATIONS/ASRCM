package gov.va.med.srcalc.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CollectionUtilsTest
{
    // Experimenting with BDD-style test names here.
    
    @Test
    public final void shouldTruncateListAboveMax()
    {
        final int maxSize = 5;
        final List<String> fullList = Arrays.asList(
                "Alpha", "Beta", "Delta", "Gamma", "Epsilon", "Zeta", "Eta");
        assertEquals(maxSize, CollectionUtils.truncateList(fullList, maxSize).size());
    }
    
    @Test
    public final void shouldTruncateListBelowMax()
    {
        final int maxSize = 5;
        final List<String> fullList = Arrays.asList("Alpha", "Beta", "Delta");
        assertEquals(fullList.size(), CollectionUtils.truncateList(fullList, maxSize).size());
    }
    
}

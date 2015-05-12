package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.VariableGroup;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class VariableGroupTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(VariableGroup.class)
            .suppress(Warning.NONFINAL_FIELDS)  // VariableGroup is not immutable
            .verify();
    }
    
    @Test
    public final void testCompareTo()
    {
        final VariableGroup a = new VariableGroup("aaa", 0);
        final VariableGroup b = new VariableGroup("bbb", 1);
        final VariableGroup c = new VariableGroup("ccc", 1); // Note duplicate order
        
        assertEquals("a should equal itself", 0, a.compareTo(a));
        assertTrue("a should be less than b", a.compareTo(b) < 0);
        assertTrue("b should be greater than a", b.compareTo(a) > 0);
        assertTrue("b should be less than c", b.compareTo(c) < 0);
        assertTrue("c should be greater than b", c.compareTo(b) > 0);
    }

    @Test
    public final void testToString()
    {
        final VariableGroup a = new VariableGroup("aaa", 0);
        assertEquals("aaa", a.toString());

    }
    
}

package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class VariableEntryTest
{
    @Test
    public final void testToString()
    {
        final VariableEntry entry = new VariableEntry();
        entry.getDynamicValues().put("one", "1");
        entry.getDynamicValues().put("two", "2");
        assertThat(
                entry.toString(),
                allOf(containsString("one=1"), containsString("two=2")));
    }
    
}

package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable;

import org.junit.Test;

public class VariableSummaryTest
{
    @Test
    public final void testRadio()
    {
        final MultiSelectVariable var = SampleObjects.functionalStatusVariable();
        final VariableSummary summary = VariableSummary.fromVariable(var);
        assertEquals(var.getDisplayName(), summary.getDisplayName());
        assertEquals("Radio Button", summary.getTypeName());
    }
    
}

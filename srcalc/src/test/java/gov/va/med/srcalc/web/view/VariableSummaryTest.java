package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;
import gov.va.med.srcalc.domain.model.NumericalVariable;
import gov.va.med.srcalc.domain.model.SampleModels;

import org.junit.Test;

public class VariableSummaryTest
{
    @Test
    public final void testRadio()
    {
        final MultiSelectVariable var = SampleModels.functionalStatusVariable();
        final VariableSummary summary = VariableSummary.fromVariable(var);
        assertEquals(var.getDisplayName(), summary.getDisplayName());
        assertEquals("Radio Button", summary.getTypeName());
        assertEquals("[Independent, Partially dependent, Totally dependent]", summary.getOptionString());
    }
    
    @Test
    public final void testNumerical()
    {
        final NumericalVariable var = SampleModels.ageVariable();
        final VariableSummary summary = VariableSummary.fromVariable(var);
        assertEquals(var.getDisplayName(), summary.getDisplayName());
        assertEquals("Numerical", summary.getTypeName());
        assertEquals("[0.0, 999.0]", summary.getOptionString());
    }
}

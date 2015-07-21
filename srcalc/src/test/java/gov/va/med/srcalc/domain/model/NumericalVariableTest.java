package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.model.NumericalVariable;
import gov.va.med.srcalc.test.util.TestHelpers;

import org.junit.Test;

/**
 * Tests the {@link NumericalVariable} class.
 */
public class NumericalVariableTest
{
    @Test(expected = NullPointerException.class)
    public final void testSetUnitsNull()
    {
        final NumericalVariable var = SampleModels.ageVariable();
        var.setUnits(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetUnitsTooLong()
    {
        final NumericalVariable var = SampleModels.ageVariable();
        var.setUnits(TestHelpers.stringOfLength(NumericalVariable.UNITS_MAX + 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testSetUnitsInvalidCharacters()
    {
        final NumericalVariable var = SampleModels.ageVariable();
        var.setUnits("\t");
    }
    
    
}

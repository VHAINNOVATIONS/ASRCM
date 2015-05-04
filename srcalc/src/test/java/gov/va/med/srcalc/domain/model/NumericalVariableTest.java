package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.model.NumericalVariable;

import org.junit.Test;

public class NumericalVariableTest
{
    @Test(expected = NullPointerException.class)
    public final void testSetDisplayNameNull()
    {
        final NumericalVariable var = SampleModels.ageVariable();
        var.setDisplayName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetDisplayNameTooLong()
    {
        final NumericalVariable var = SampleModels.ageVariable();
        var.setDisplayName(
                // 81 characters
                "01234567890123456789012345678901234567890123456789012345678901234567890123456789X");
    }

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
        var.setUnits(
                // 41 characters
                "0123456789012345678901234567890123456789X");
    }
    
    
}

package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.model.NumericalVariable;

import org.junit.Test;

public class NumericalVariableTest
{
    @Test(expected = NullPointerException.class)
    public final void testSetUnitsNull()
    {
        final NumericalVariable var = SampleObjects.sampleAgeVariable();
        var.setUnits(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetUnitsTooLong()
    {
        final NumericalVariable var = SampleObjects.sampleAgeVariable();
        var.setUnits(
                // 41 characters
                "0123456789012345678901234567890123456789X");
    }
    
    
}

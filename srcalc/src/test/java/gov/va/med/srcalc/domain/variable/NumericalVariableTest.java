package gov.va.med.srcalc.domain.variable;

import gov.va.med.srcalc.domain.SampleObjects;

import org.junit.Test;

public class NumericalVariableTest
{
    @Test(expected = IllegalArgumentException.class)
    public final void testSetDisplayNameTooLong()
    {
        final NumericalVariable var = SampleObjects.sampleAgeVariable();
        var.setDisplayName(
                // 81 characters
                "01234567890123456789012345678901234567890123456789012345678901234567890123456789X");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetg()
    {
        final NumericalVariable var = SampleObjects.sampleAgeVariable();
        var.setUnits(
                // 41 characters
                "0123456789012345678901234567890123456789X");
    }
    
    
}

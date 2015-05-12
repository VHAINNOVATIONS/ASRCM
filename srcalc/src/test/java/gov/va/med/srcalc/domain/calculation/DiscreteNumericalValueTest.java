package gov.va.med.srcalc.domain.calculation;

import static org.junit.Assert.*;
import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.domain.calculation.DiscreteNumericalValue;
import gov.va.med.srcalc.domain.model.*;

import org.junit.Test;

public class DiscreteNumericalValueTest
{
    private DiscreteNumericalVariable.Category findCategory(
            DiscreteNumericalVariable var, String catName)
    {
        for (final DiscreteNumericalVariable.Category cat : var.getCategories())
        {
            if (cat.getOption().getValue().equals("WNL"))
            {
                return cat;
            }
        }
        return null;
    }

    @Test
    public final void testPresumed()
    {
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        // Find the WNL Category.
        final DiscreteNumericalVariable.Category wnl = findCategory(var, "WNL");
        final DiscreteNumericalValue val = DiscreteNumericalValue.fromCategory(var, wnl);
        // getVariable()
        assertSame(var, val.getVariable());
        // toString()
        assertEquals("White Blood Count = WNL[range=(-1.0E12, 11.0]]", val.toString());
        // getDisplayString()
        assertEquals("Presumed WNL", val.getDisplayString());
    }
    
    @Test
    public final void testNumericalValid() throws Exception
    {
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        final DiscreteNumericalValue val =
                DiscreteNumericalValue.fromNumerical(var, 2.0f);
        // getVariable()
        assertSame(var, val.getVariable());
        // toString()
        assertEquals("White Blood Count = WNL[range=(-1.0E12, 11.0]]", val.toString());
        // getDisplayString()
        assertEquals("WNL (Actual Value: 2.0)", val.getDisplayString());
    }
    
    @Test(expected = ValueTooHighException.class)
    public final void testNumericalTooHigh() throws Exception
    {
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        DiscreteNumericalValue.fromNumerical(var, 50.1f);
    }
    
    @Test(expected = ValueTooLowException.class)
    public final void testNumericalTooLow() throws Exception
    {
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        DiscreteNumericalValue.fromNumerical(var, 1.0f);
    }
    
    @Test(expected = ValueTooLowException.class)
    public final void testNumericalLowBoundInclusiveFail() throws Exception
    {
    	// There is an accompanying test for Upper Bound in {@link NumericalValueTest}
    	final DiscreteNumericalVariable var = SampleModels.wbcVariable();
    	var.setMinInclusive(false);
        DiscreteNumericalValue.fromNumerical(var, 2.0f);
    }
    
    @Test
    public final void testNumericalLowBoundInclusivePass() throws Exception
    {
    	// There is an accompanying test for Upper Bound in {@link NumericalValueTest}
    	final DiscreteNumericalVariable var = SampleModels.wbcVariable();
    	// Should not throw an exception
    	DiscreteNumericalValue.fromNumerical(var, 2.0f);
    }
    
    @Test(expected = ConfigurationException.class)
    public final void testNumericalMisconfigured() throws Exception
    {
        final DiscreteNumericalVariable var = SampleModels.misconfiguredWbcVariable();
        DiscreteNumericalValue.fromNumerical(var, 10.5f);
    }
    
}

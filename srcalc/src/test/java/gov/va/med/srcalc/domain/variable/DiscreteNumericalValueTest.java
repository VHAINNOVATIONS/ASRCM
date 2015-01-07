package gov.va.med.srcalc.domain.variable;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;

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
        final DiscreteNumericalVariable var = SampleObjects.wbcVariable();
        // Find the WNL Category.
        final DiscreteNumericalVariable.Category wnl = findCategory(var, "WNL");
        final DiscreteNumericalValue val = new DiscreteNumericalValue(var, wnl);
        // getVariable()
        assertSame(var, val.getVariable());
        // toString()
        assertEquals("White Blood Count = WNL[range=(-1.0E12, 11.0]]", val.toString());
        // getDisplayString()
        assertEquals("Presumed WNL", val.getDisplayString());
    }
    
}

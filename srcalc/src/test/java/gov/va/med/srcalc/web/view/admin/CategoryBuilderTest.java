package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.domain.model.MultiSelectOption;

import org.junit.Test;

/**
 * Tests the {@link CategoryBuilder} class.
 */
public class CategoryBuilderTest
{
    @Test
    public final void testBuild()
    {
        final CategoryBuilder builder = new CategoryBuilder();
        
        // Test default value.
        assertEquals("", builder.getValue());
        
        // Test building with the default range.
        final String value = "firstValue";
        assertSame(builder, builder.setValue(value)); // note: modifying builder
        assertEquals(
                new Category(new MultiSelectOption(value), 0f, true),
                builder.build());
        
        // Test building with a different range.
        final float upperBound = -2.1f;
        final boolean upperInclusive = false;
        assertSame(builder, builder.setUpperBound(upperBound));
        assertEquals(upperBound, builder.getUpperBound(), 0.0f);
        assertSame(builder, builder.setUpperInclusive(upperInclusive));
        assertEquals(upperInclusive, builder.getUpperInclusive());
        assertEquals(
                new Category(new MultiSelectOption(value), upperBound, upperInclusive),
                builder.build());
    }
    
    @Test
    public final void testFromPrototype()
    {
        final Category expectedCategory = new Category(
                        new MultiSelectOption("theValue"), 1.0f, false);
        
        final CategoryBuilder builder = CategoryBuilder.fromPrototype(expectedCategory);
        
        assertEquals(expectedCategory, builder.build());
    }
    
}

package gov.va.med.srcalc.domain.variable;

import static org.junit.Assert.*;

import java.util.*;

import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.DiscreteNumericalVariable.Category;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class DiscreteNumericalVariableTest
{
    @Test
    public final void testCategoryEquals()
    {
        EqualsVerifier.forClass(DiscreteNumericalVariable.Category.class)
            // The public interface is immutable and does not permit null fields.
            .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS)
            .verify();
    }
    
    @Test
    public final void testCategoryOrdering()
    {
        final Category cat1 = new Category(
                new NumericalRange(1.0f, true, 4.0f, false), new MultiSelectOption("one"));
        final Category cat2 = new Category(
                new NumericalRange(4.0f, true, 5.0f, false), new MultiSelectOption("two"));
        final Category cat3 = new Category(
                new NumericalRange(5.0f, true, 6.0f, false), new MultiSelectOption("one"));
        // Intentionally put the Categories out of order in this list.
        final List<Category> cats = Arrays.asList(cat3, cat2, cat1);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "Creatinine", SampleObjects.labVariableGroup(), new TreeSet<>(cats));
        
        // Behavior verification. The constructed ArrayList should have the
        // right order.
        final List<Category> orderedCats = new ArrayList<>(var.getCategories());
        assertEquals(Arrays.asList(cat1, cat2, cat3), orderedCats);
    }
}

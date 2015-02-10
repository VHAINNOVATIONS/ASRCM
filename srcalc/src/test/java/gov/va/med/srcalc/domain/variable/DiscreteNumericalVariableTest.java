package gov.va.med.srcalc.domain.variable;

import static org.junit.Assert.*;

import java.util.*;

import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.test.util.TestHelpers;
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
        // WNL should always be at the start of the list after sorting
        final Category catWnl = new Category(
        		new NumericalRange(4.0f, true, 4.5f, false), new MultiSelectOption("WNL"));
        final Category cat2 = new Category(
                new NumericalRange(4.5f, true, 6.0f, false), new MultiSelectOption("three"));
        // Intentionally put the Categories out of order in this list.
        final List<Category> cats = Arrays.asList(cat2, cat1, catWnl);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "Creatinine", SampleObjects.labVariableGroup(), new TreeSet<>(cats), "creatinine");
        
        // Behavior verification. The constructed ArrayList should have the
        // right order.
        TestHelpers.verifyCompareToContract(catWnl, cat1, cat2);
        final List<Category> orderedCats = new ArrayList<>(var.getCategories());
        assertEquals(Arrays.asList(catWnl, cat1, cat2), orderedCats);
    }
    
    public final void testGetOptions()
    {
        final Category cat1 = new Category(
                new NumericalRange(1.0f, true, 4.0f, false), new MultiSelectOption("one"));
        final Category cat2 = new Category(
                new NumericalRange(4.0f, true, 5.0f, false), new MultiSelectOption("two"));
        final Category cat3 = new Category(
                new NumericalRange(5.0f, true, 6.0f, false), new MultiSelectOption("three"));
        // Intentionally put the Categories out of order in this list.
        final List<Category> cats = Arrays.asList(cat3, cat2, cat1);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "Creatinine", SampleObjects.labVariableGroup(), new TreeSet<>(cats), "creatinine");
        
        assertEquals(
                Arrays.asList(cat1.getOption(), cat2.getOption(), cat3.getOption()),
                var.getOptions());
    }
}

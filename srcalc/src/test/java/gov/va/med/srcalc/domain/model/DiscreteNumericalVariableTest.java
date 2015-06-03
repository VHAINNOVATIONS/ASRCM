package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.test.util.TestHelpers;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

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
        final Category catWnl = new Category(
                new NumericalRange(4.0f, true, 4.5f, false), new MultiSelectOption("WNL"));
        final Category cat3 = new Category(
                new NumericalRange(4.5f, true, 6.0f, false), new MultiSelectOption("three"));
        // Intentionally put the Categories out of order in this set.
        final ImmutableSet<Category> cats = ImmutableSet.of(cat3, catWnl, cat1);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "Creatinine", SampleModels.labVariableGroup(), cats, "creatinine");
        
        // Behavior verification.
        TestHelpers.verifyCompareToContract(cat1, catWnl, cat3);
        assertEquals(
                ImmutableList.of(cat1, catWnl, cat3),
                // Convert the sorted set into a List for order comparison.
                ImmutableList.copyOf(var.getCategories()));
        
        // Also test getCategoriesWnlFirst().
        assertEquals(
                ImmutableList.of(catWnl, cat1, cat3),
                var.getCategoriesWnlFirst().asList());
    }
    
    @Test
    public final void testGetOptions()
    {
        final Category cat1 = new Category(
                new NumericalRange(1.0f, true, 4.0f, false), new MultiSelectOption("one"));
        final Category cat2 = new Category(
                new NumericalRange(4.0f, true, 5.0f, false), new MultiSelectOption("two"));
        final Category cat3 = new Category(
                new NumericalRange(5.0f, true, 6.0f, false), new MultiSelectOption("three"));
        // Intentionally put the Categories out of order in this set.
        final ImmutableSet<Category> cats = ImmutableSet.of(cat3, cat2, cat1);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "Creatinine", SampleModels.labVariableGroup(), cats, "creatinine");
        
        assertEquals(
                ImmutableList.of(cat1.getOption(), cat2.getOption(), cat3.getOption()),
                var.getOptions());
    }
}

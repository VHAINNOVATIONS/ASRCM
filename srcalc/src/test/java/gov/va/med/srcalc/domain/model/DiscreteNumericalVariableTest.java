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
    public final void testCategories()
    {
        final Category cat1 = new Category(new MultiSelectOption("one"), 4.0f, false);
        final Category catWnl = new Category(new MultiSelectOption("WNL"), 4.5f, false);
        final Category cat3 = new Category(new MultiSelectOption("three"), 6.0f, false);
        // Note: this category includes the bound so it's a little higher.
        final Category cat4 = new Category(new MultiSelectOption("four"), 6.0f, true);
        // Intentionally put the Categories out of order in this set.
        final ImmutableSet<Category> cats = ImmutableSet.of(cat3, cat4, catWnl, cat1);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "Creatinine", SampleModels.labVariableGroup(), cats, "creatinine");
        
        // Behavior verification.
        TestHelpers.verifyCompareToContract(cat1, cat3, cat4);
        assertEquals(
                ImmutableList.of(cat1, catWnl, cat3, cat4),
                // Convert the sorted set into a List for order comparison.
                ImmutableList.copyOf(var.getCategories()));
        
        // Test getContainingCategory().
        assertEquals(cat1, var.getContainingCategory(1.0f));
        assertEquals(catWnl, var.getContainingCategory(4.0f));
        assertEquals(cat3, var.getContainingCategory(4.6f));
        assertEquals(cat4, var.getContainingCategory(6.0f));
        
        // Also test getCategoriesWnlFirst().
        assertEquals(
                ImmutableList.of(catWnl, cat1, cat3, cat4),
                var.getCategoriesWnlFirst().asList());

        // Real-world categories should never have the same upper bound and inclusivity,
        // but make sure we handle this case anyway.
        final Category cat5 = new Category(new MultiSelectOption("five"), 6.0f, true);
        assertNotEquals(cat4, cat5);
        // The string "four" is greater than "five"
        assertTrue(cat4.compareTo(cat5) > 0);
    }
    
    @Test
    public final void testGetOptions()
    {
        final Category cat1 = new Category(new MultiSelectOption("one"), 4.0f, false);
        final Category cat2 = new Category(new MultiSelectOption("two"), 5.0f, false);
        final Category cat3 = new Category(new MultiSelectOption("three"), 6.0f, false);
        // Intentionally put the Categories out of order in this set.
        final ImmutableSet<Category> cats = ImmutableSet.of(cat3, cat2, cat1);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "Creatinine", SampleModels.labVariableGroup(), cats, "creatinine");
        
        assertEquals(
                ImmutableList.of(cat1.getOption(), cat2.getOption(), cat3.getOption()),
                var.getOptions());
    }
}

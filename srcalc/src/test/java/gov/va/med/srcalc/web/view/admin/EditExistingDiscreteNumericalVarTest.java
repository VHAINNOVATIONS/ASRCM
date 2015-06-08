package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;

import java.util.SortedSet;

import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.service.MockModelService;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

/**
 * Tests {@link EditExistingDiscreteNumericalVar}.
 */
public class EditExistingDiscreteNumericalVarTest
{
    private final MockModelService fModelService = new MockModelService();

    @Test
    public final void testEditExistingVarMethods()
    {
        final EditExistingDiscreteNumericalVar ev = new EditExistingDiscreteNumericalVar(
                SampleModels.wbcVariable(), fModelService);
        EditExistingVarTest.testEditExistingVarMethods(ev);
    }
    
    @Test
    public final void testApplyToVariableNoMods()
    {
        final DiscreteNumericalVariable var = SampleModels.cardiacAgeVariable();
        // Make a copy of the original properties.
        final String origDisplayName = var.getDisplayName();
        final VariableGroup origGroup = var.getGroup();
        final Optional<String> origHelpText = var.getHelpText();
        final ValueRetriever origRetriever = var.getRetriever();
        final String origUnits = var.getUnits();
        final NumericalRange origRange = var.getValidRange();
        final SortedSet<Category> origCategories = var.getCategories();
        
        // Behavior
        final EditExistingDiscreteNumericalVar ev =
                new EditExistingDiscreteNumericalVar(var, fModelService);
        final DiscreteNumericalVariable modifiedVar = ev.applyToVariable();
        
        // Verification
        assertSame(var, modifiedVar);
        assertEquals(origDisplayName, var.getDisplayName());
        assertEquals(origGroup, var.getGroup());
        assertEquals(origHelpText, var.getHelpText());
        assertEquals(origRetriever, var.getRetriever());
        assertEquals(origUnits, var.getUnits());
        assertEquals(origRange, var.getValidRange());
        assertEquals(origCategories, var.getCategories());
    }
    
    @Test
    public final void testApplyToVariableAllMods()
    {
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        final String displayName = "dnDisplayName";
        final VariableGroup group = fModelService.getAllVariableGroups().asList().get(2);
        final String helpText = "dnHelpText";
        final ValueRetriever retriever = ValueRetriever.SGOT;
        final String units = "transferases";
        final NumericalRange validRange = new NumericalRange(1.0f, false, 91.1f, true);
        final ImmutableSet<Category> categories = ImmutableSet.of(
                new Category(new MultiSelectOption("low"), 10.0f, true),
                new Category(new MultiSelectOption("medium"), 50.1f, true),
                new Category(new MultiSelectOption("high"), 90.0f, true));
        
        // Behavior
        final EditExistingDiscreteNumericalVar ev = new EditExistingDiscreteNumericalVar(
                var, fModelService);
        // Set the basic properties and test the getters.
        EditBaseVarTest.testSetProperties(
                ev, displayName, group.getId(), helpText, retriever);
        ev.setUnits(units);
        ev.getValidRange()
            .setLowerBound(validRange.getLowerBound())
            .setLowerInclusive(validRange.isLowerInclusive())
            .setUpperBound(validRange.getUpperBound())
            .setUpperInclusive(validRange.isUpperInclusive());
        ev.getCategories().clear();
        for (final Category category : categories)
        {
            ev.getCategories().add(CategoryBuilder.fromPrototype(category));
        }
        final DiscreteNumericalVariable modifiedVar = ev.applyToVariable();
        
        // Verification
        assertSame(var, modifiedVar);
        assertEquals(displayName, var.getDisplayName());
        assertEquals(group, var.getGroup());
        assertEquals(helpText, var.getHelpText().get());
        assertEquals(retriever, var.getRetriever());
        assertEquals(units, var.getUnits());
        assertEquals(validRange, var.getValidRange());
        assertEquals(categories, var.getCategories());
    }
    
}

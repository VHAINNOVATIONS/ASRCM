package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.web.view.Views;

import org.junit.Test;

import com.google.common.collect.ImmutableSortedSet;

public class EditDiscreteNumericalVarTest
{
    private final MockModelService fModelService = new MockModelService();

    @Test
    public final void testBasic()
    {
        final EditDiscreteNumericalVar ev = new EditDiscreteNumericalVar(fModelService);
        
        // Verify the default of 2 categories.
        assertEquals(2, ev.getCategories().size());
        
        // EditDiscreteNumericalVar doesn't specify what it returns for
        // getTypeName(), just make sure it returns a non-empty string.
        assertThat(ev.getTypeName(), not(isEmptyOrNullString()));
        
        assertEquals(Views.NEW_DISCRETE_NUMERICAL_VARIABLE, ev.getNewViewName());
        
        assertEquals(DiscreteNumericalVariable.UNITS_MAX, ev.getUnitsMax());
    }

    @Test
    public final void testBuildNew()
    {
        // Values to set
        final String key = "dnKey";
        final String displayName = "dnDisplayName";
        // Just pick an arbitrary group.
        final VariableGroup group = fModelService.getAllVariableGroups().asList().get(2);
        final String helpText = "dnHelpText";
        final ValueRetriever retriever = ValueRetriever.BMI;
        final String units = "x1000mm^3";
        final NumericalRange validRange = new NumericalRange(-534.1f, false, 123.1f, true);
        final Category category1 = new Category(
                new MultiSelectOption("category1"), -10.0f, true);
        final Category category2 = new Category(
                new MultiSelectOption("category2"), 100.0f, true);
        final Category category3 = new Category(
                new MultiSelectOption("category3"), 123.1f, true);
        
        // Behavior
        final EditDiscreteNumericalVar ev = new EditDiscreteNumericalVar(fModelService);
        ev.setKey(key);
        ev.setDisplayName(displayName);
        ev.setGroupId(group.getId());
        ev.setHelpText(helpText);
        ev.setRetriever(retriever);
        ev.setUnits(units);
        ev.getValidRange().setLowerBound(validRange.getLowerBound());
        ev.getValidRange().setLowerInclusive(validRange.isLowerInclusive());
        ev.getValidRange().setUpperBound(validRange.getUpperBound());
        ev.getValidRange().setUpperInclusive(validRange.isUpperInclusive());
        ev.getCategories().clear();
        ev.getCategories().add(CategoryBuilder.fromPrototype(category1));
        ev.getCategories().add(CategoryBuilder.fromPrototype(category2));
        ev.getCategories().add(CategoryBuilder.fromPrototype(category3));
        final DiscreteNumericalVariable createdVariable = ev.buildNew();
        
        // Verification
        assertEquals(key, createdVariable.getKey());
        assertEquals(displayName, createdVariable.getDisplayName());
        assertEquals(group, createdVariable.getGroup());
        assertEquals(helpText, createdVariable.getHelpText().get());
        assertEquals(retriever, createdVariable.getRetriever());
        assertEquals(
                ImmutableSortedSet.of(category1, category2, category3),
                createdVariable.getCategories());
    }
    
    @Test(expected = IllegalStateException.class)
    public final void testTooFewCategories()
    {
        final EditDiscreteNumericalVar ev = new EditDiscreteNumericalVar(fModelService);
        final Category category1 = new Category(
                new MultiSelectOption("category1"), 10.0f, true);
        ev.getCategories().clear();
        ev.getCategories().add(CategoryBuilder.fromPrototype(category1));
        
        ev.buildNew();
    }
    
}

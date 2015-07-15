package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;

import java.util.List;

import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.model.MultiSelectVariable.DisplayType;
import gov.va.med.srcalc.service.MockModelService;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

/**
 * Tests the {@link EditExistingMultiSelectVar} class.
 */
public class EditExistingMultiSelectVarTest
{
    private final MockModelService fModelService = new MockModelService();

    @Test
    public final void testEditExistingVarMethods()
    {
        final EditExistingMultiSelectVar ev = new EditExistingMultiSelectVar(
                SampleModels.genderVariable(), fModelService);
        EditExistingVarTest.testEditExistingVarMethods(ev);
    }
    
    @Test
    public final void testApplyToVariableNoMods()
    {
        final MultiSelectVariable var = SampleModels.genderVariable();
        // Make a copy of the original properties.
        final String origDisplayName = var.getDisplayName();
        final VariableGroup origGroup = var.getGroup();
        final Optional<String> origHelpText = var.getHelpText();
        final ValueRetriever origRetriever = var.getRetriever();
        final DisplayType origDisplayType = var.getDisplayType();
        final List<MultiSelectOption> origOptions = var.getOptions();
        
        // Behavior
        final EditExistingMultiSelectVar ev = new EditExistingMultiSelectVar(
                var, fModelService);
        final MultiSelectVariable modifiedVar = ev.applyToVariable();
        
        // Verification
        assertSame(var, modifiedVar);
        assertEquals(origDisplayName, var.getDisplayName());
        assertEquals(origGroup, var.getGroup());
        assertEquals(origHelpText, var.getHelpText());
        assertEquals(origRetriever, var.getRetriever());
        assertEquals(origDisplayType, var.getDisplayType());
        assertEquals(origOptions, var.getOptions());
    }
    
    @Test
    public final void testApplyToVariableAllMods()
    {
        final MultiSelectVariable var = SampleModels.genderVariable();
        final String displayName = "msDisplayName";
        final VariableGroup group = fModelService.getAllVariableGroups().iterator().next();
        final String helpText = "msHelpText";
        final ValueRetriever retriever = null;
        final MultiSelectVariable.DisplayType displayType =
                MultiSelectVariable.DisplayType.Dropdown;
        final ImmutableList<String> options =
                ImmutableList.of("option1", "option2", "option3");
        final ImmutableList<MultiSelectOption> multiSelectOptions = ImmutableList.of(
                new MultiSelectOption(options.get(0)),
                new MultiSelectOption(options.get(1)),
                new MultiSelectOption(options.get(2)));
        
        // Behavior
        final EditExistingMultiSelectVar ev = new EditExistingMultiSelectVar(
                var, fModelService);
        // Set the basic properties and test the getters.
        EditBaseVarTest.testSetProperties(
                ev, displayName, group.getId(), helpText, retriever);
        ev.setDisplayType(displayType);
        ev.getOptions().clear();
        ev.getOptions().addAll(options);
        final MultiSelectVariable modifiedVar = ev.applyToVariable();
        
        // Verification
        assertSame(var, modifiedVar);
        assertEquals(displayName, var.getDisplayName());
        assertEquals(group, var.getGroup());
        assertEquals(helpText, var.getHelpText().get());
        assertEquals(retriever, var.getRetriever());
        assertEquals(displayType, var.getDisplayType());
        assertEquals(multiSelectOptions, var.getOptions());
    }
    
}

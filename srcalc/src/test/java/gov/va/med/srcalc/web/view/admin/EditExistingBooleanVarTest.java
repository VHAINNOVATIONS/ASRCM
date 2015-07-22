package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.MockModelService;

import org.junit.Test;

/**
 * Tests the {@link EditExistingBooleanVar} class.
 */
public class EditExistingBooleanVarTest
{
    private final MockModelService fModelService = new MockModelService();

    @Test
    public final void testEditExistingVarMethods()
    {
        final EditExistingBooleanVar ev = new EditExistingBooleanVar(
                SampleModels.dnrVariable(), fModelService);
        EditExistingVarTest.testEditExistingVarMethods(ev);
    }

    @Test
    public final void testApplyToVariable()
    {
        final String newDisplayName = "newName";
        final String newHelpText = "newHelpText";
        
        // Setup
        final BooleanVariable var = SampleModels.dnrVariable();
        final VariableGroup newGroup = fModelService.getAllVariableGroups().iterator().next();
        // Ensure we're actually going to test something here.
        assertNotEquals(newGroup, var.getGroup());
        
        // Operation
        final EditExistingBooleanVar ev =
                new EditExistingBooleanVar(var, fModelService);
        EditBaseVarTest.testSetProperties(
                ev, newDisplayName, newGroup.getId(), newHelpText, null);
        ev.applyToVariable();
        
        // Verification
        assertEquals(var, ev.getTargetVariable());
        assertEquals(newDisplayName, var.getDisplayName());
        assertEquals(newHelpText, var.getHelpText().get());
        assertEquals(newGroup, var.getGroup());
        
        // Also test the empty string -> absent translation
        ev.setHelpText("");
        ev.applyToVariable();
        assertFalse(var.getHelpText().isPresent());
    }
    
    @Test(expected = IllegalStateException.class)
    public final void testApplyKeyChange()
    {
        final BooleanVariable var = SampleModels.dnrVariable();
        final EditExistingBooleanVar ev =
                new EditExistingBooleanVar(var, fModelService);
        ev.setKey("newKey");
        ev.applyToVariable();
    }
    
}

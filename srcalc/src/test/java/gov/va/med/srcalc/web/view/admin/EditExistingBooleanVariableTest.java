package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.MockModelService;

import org.junit.Test;

public class EditExistingBooleanVariableTest
{
    private final MockModelService fModelService = new MockModelService();

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
        final EditExistingBooleanVariable ev =
                new EditExistingBooleanVariable(var, fModelService);
        ev.setDisplayName(newDisplayName);
        ev.setHelpText(newHelpText);
        ev.setGroupId(newGroup.getId());
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
        final EditExistingBooleanVariable ev =
                new EditExistingBooleanVariable(var, fModelService);
        ev.setKey("newKey");
        ev.applyToVariable();
    }
    
}

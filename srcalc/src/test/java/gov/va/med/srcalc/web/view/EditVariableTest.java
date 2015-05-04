package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.*;

import java.util.SortedSet;

import org.junit.Test;
import static org.junit.Assert.*;

public class EditVariableTest
{
    @Test
    public final void testSetGroupValid()
    {
        // Setup
        final AbstractVariable var = SampleModels.dnrVariable();
        final SortedSet<VariableGroup> allGroups = SampleModels.variableGroups();
        final VariableGroup newGroup = allGroups.first();
        final EditVariable ev = new EditVariable(var, allGroups);

        // Ensure we're actually going to test something here.
        assertNotEquals(newGroup.getId(), ev.getGroupId());
        
        // Operation
        ev.setGroupId(newGroup.getId());
        
        // Verification
        assertEquals(newGroup.getId(), ev.getGroupId());
        assertEquals(newGroup, ev.getGroup().get());
    }
    
    @Test
    public final void testSetGroupInvalid()
    {
        final AbstractVariable var = SampleModels.dnrVariable();
        final EditVariable ev = new EditVariable(var, SampleModels.variableGroups());
        
        ev.setGroupId(90);
        
        assertFalse("Optional should not contain a group", ev.getGroup().isPresent());
    }
    
    @Test
    public final void testSetOntoVariable()
    {
        final String newDisplayName = "newName";
        final String newHelpText = "newHelpText";
        
        // Setup
        final AbstractVariable var = SampleModels.dnrVariable();
        final SortedSet<VariableGroup> allGroups = SampleModels.variableGroups();
        final VariableGroup newGroup = allGroups.first();
        // Ensure we're actually going to test something here.
        assertNotEquals(newGroup, var.getGroup());
        final EditVariable ev = new EditVariable(var, allGroups);
        
        // Operation
        ev.setDisplayName(newDisplayName);
        ev.setHelpText(newHelpText);
        ev.setGroupId(newGroup.getId());
        ev.applyToVariable(var);
        
        // Verification
        assertEquals(newDisplayName, var.getDisplayName());
        assertEquals(newHelpText, var.getHelpText());
        assertEquals(newGroup, var.getGroup());
    }
}

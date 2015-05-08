package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.*;

import java.util.Arrays;
import java.util.SortedSet;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

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
    public final void testCalculateDependentModels()
    {
        // Setup
        final AbstractVariable var = SampleModels.dnrVariable();
        final SortedSet<VariableGroup> allGroups = SampleModels.variableGroups();
        final RiskModel thoracicModel = SampleModels.thoracicRiskModel();
        final RiskModel emptyModel = new RiskModel("Empty model");
        final EditVariable ev = new EditVariable(var, allGroups);
        
        // Operation
        ev.calculateDependentModels(Arrays.asList(thoracicModel, emptyModel));
        
        // Verification
        assertEquals(
                ImmutableSet.of(thoracicModel),
                ev.getDependentModels());
    }
    
    @Test
    public final void testApply()
    {
        final String newDisplayName = "newName";
        final String newHelpText = "newHelpText";
        
        // Setup
        final AbstractVariable var = SampleModels.dnrVariable();
        final SortedSet<VariableGroup> allGroups = SampleModels.variableGroups();
        final VariableGroup newGroup = allGroups.first();
        // Ensure we're actually going to test something here.
        assertNotEquals(newGroup, var.getGroup());
        
        // Operation
        final EditVariable ev = new EditVariable(var, allGroups);
        ev.setDisplayName(newDisplayName);
        ev.setHelpText(newHelpText);
        ev.setGroupId(newGroup.getId());
        ev.applyToVariable();
        
        // Verification
        assertEquals(newDisplayName, var.getDisplayName());
        assertEquals(newHelpText, var.getHelpText().get());
        assertEquals(newGroup, var.getGroup());
        
        // Also test the empty string -> absent translation
        ev.setHelpText("");
        ev.applyToVariable();
        assertFalse(var.getHelpText().isPresent());
    }
}

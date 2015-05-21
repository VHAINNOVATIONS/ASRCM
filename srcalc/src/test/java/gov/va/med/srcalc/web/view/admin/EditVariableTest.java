package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.web.view.admin.EditBooleanVariable;
import gov.va.med.srcalc.web.view.admin.EditVariable;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import static org.junit.Assert.*;

/**
 * {@link EditVariable} is abstract, but this suite tests the concrete
 * functionality.
 */
public class EditVariableTest
{
    private final MockModelService fModelService = new MockModelService();

    @Test
    public final void testSetGroupValid()
    {
        // Setup
        final VariableGroup newGroup = fModelService.getAllVariableGroups().iterator().next();
        // Use an EditBooleanVariable to test since it is a very basic
        // implementation of EditVariable.
        final EditVariable ev = new EditBooleanVariable(fModelService);

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
        // Use an EditBooleanVariable to test since it is a very basic
        // implementation of EditVariable.
        final EditVariable ev = new EditBooleanVariable(fModelService);
        
        ev.setGroupId(90);
        
        assertFalse("Optional should not contain a group", ev.getGroup().isPresent());
    }
    
    @Test
    public final void testCalculateDependentModels()
    {
        // Setup
        final BooleanVariable var = SampleModels.dnrVariable();
        final EditVariable ev = new EditBooleanVariable(var, fModelService);
        
        // Verification
        assertEquals(
                ImmutableSet.of(fModelService.getThoracicModel()),
                ev.getDependentModels());
    }
}

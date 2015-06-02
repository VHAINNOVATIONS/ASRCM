package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.MockModelService;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import static org.junit.Assert.*;

/**
 * Test cases for {@link EditBaseVar} methods. Test classes for concrete
 * EditBaseVar implementations should extend this class.
 */
public class EditBaseVarTest
{
    private final MockModelService fModelService = new MockModelService();
    
    @Test
    public final void testReferenceData()
    {
        // Use an EditBooleanVar to test since it is a very basic
        // implementation of EditBaseVar.
        final EditBaseVar ev = new EditBooleanVar(fModelService);
        
        assertEquals(Variable.KEY_MAX, ev.getKeyMax());
        assertEquals(Variable.DISPLAY_NAME_MAX, ev.getDisplayNameMax());
        assertEquals(fModelService.getAllVariableGroups(), ev.getAllGroups());
    }

    @Test
    public final void testSetGroupValid()
    {
        // Setup
        final VariableGroup newGroup = fModelService.getAllVariableGroups().iterator().next();
        // Use an EditBooleanVar to test since it is a very basic
        // implementation of EditBaseVar.
        final EditBaseVar ev = new EditBooleanVar(fModelService);

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
        // Use an EditBooleanVar to test since it is a very basic
        // implementation of EditBaseVar.
        final EditBaseVar ev = new EditBooleanVar(fModelService);
        
        ev.setGroupId(90);
        
        assertFalse("Optional should not contain a group", ev.getGroup().isPresent());
    }
    
    @Test
    public final void testCalculateDependentModels()
    {
        // Setup
        final BooleanVariable var = SampleModels.dnrVariable();
        // Use an EditBooleanVar to test since it is a very basic
        // implementation of EditBaseVar.
        final EditBaseVar ev = new EditBooleanVar(var, fModelService);
        
        // Verification
        assertEquals(
                ImmutableSet.of(fModelService.getThoracicModel()),
                ev.getDependentModels());
    }
}

package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.BooleanVariable;
import gov.va.med.srcalc.domain.variable.Variable;

import org.junit.Before;
import org.junit.Test;

public class DefaultAdminServiceTest
{
    private List<Variable> fSampleVariables;
    
    @Before
    public final void setup()
    {
        fSampleVariables = SampleObjects.sampleVariableList();
    }
    
    public VariableDao mockVariableDao()
    {
        final VariableDao dao = mock(VariableDao.class);
        when(dao.getAllVariables()).thenReturn(fSampleVariables);
        // Use a reference to the existing DNR variable so we can do updates.
        final Variable variable = fSampleVariables.get(3);
        when(dao.getByName(variable.getDisplayName())).thenReturn(variable);
        return dao;
    }
    
    @Test
    public final void testGetAllVariables()
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(mockVariableDao());
        
        // Behavior verification.
        // Variables do not override equals() but this works because we use
        // the same Variable objects.
        assertEquals(fSampleVariables, s.getAllVariables());
    }
    
    @Test
    public final void testGetVariable() throws Exception
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(mockVariableDao());
        
        // Behavior verification.
        final BooleanVariable actualVar = (BooleanVariable)s.getVariable("DNR");
        assertEquals("DNR", actualVar.getDisplayName());
    }
    
    @Test(expected = InvalidIdentifierException.class)
    public final void testGetInvalidVariable() throws Exception
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(mockVariableDao());
        
        // Behavior verification.
        s.getVariable("Does not exist");
    }
    
    @Test
    public final void testUpdateVariable() throws Exception
    {
        final String origName = "DNR";
        final String newName = "Do Not R";
        
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(mockVariableDao());
        
        // Setup
        final Variable var = s.getVariable(origName);
        assertEquals(origName, var.getDisplayName());  // sanity check
        
        // Behavior verification.
        final EditVariable ev = EditVariable.fromVariable(var);
        assertTrue(ev.isIntegratedVariable());
        ev.setDisplayName(newName);
        s.updateVariable(origName, ev);
        // Can't test getVariable() due to limitations of the mock.
        assertEquals(newName, var.getDisplayName());
    }
    
}

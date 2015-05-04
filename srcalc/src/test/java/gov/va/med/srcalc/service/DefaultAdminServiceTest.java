package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.model.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultAdminServiceTest
{
    private List<AbstractVariable> fSampleVariables;
    
    @Before
    public final void setup()
    {
        fSampleVariables = SampleModels.sampleVariableList();
    }
    
    public VariableDao mockVariableDao()
    {
        final VariableDao dao = mock(VariableDao.class);
        when(dao.getAllVariables()).thenReturn(fSampleVariables);
        // Use a reference to the existing DNR variable so we can do updates.
        final AbstractVariable variable = fSampleVariables.get(3);
        when(dao.getByKey(variable.getKey())).thenReturn(variable);
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
        final String key = "dnr";
        
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(mockVariableDao());
        
        // Behavior verification.
        final BooleanVariable actualVar = (BooleanVariable)s.getVariable(key);
        assertEquals(key, actualVar.getKey());
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
    	final String key = "dnr";
        final String origName = "DNR";
        final String newName = "Do Not R";
        
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(mockVariableDao());
        
        // Setup
        final AbstractVariable var = s.getVariable(key);
        assertEquals(origName, var.getDisplayName());  // sanity check
        
        // Behavior verification.
        final EditVariable ev = EditVariable.fromVariable(var);
        assertTrue(ev.isIntegratedVariable());
        ev.setDisplayName(newName);
        s.updateVariable(ev);
        // Can't test getVariable() due to limitations of the mock.
        assertEquals(newName, var.getDisplayName());
    }
    
}

package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import gov.va.med.srcalc.db.RiskModelDao;
import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.model.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;

public class DefaultAdminServiceTest
{
    private List<AbstractVariable> fSampleVariables;
    private ImmutableSortedSet<VariableGroup> fSampleGroups;
    
    @Before
    public final void setup()
    {
        fSampleVariables = SampleModels.sampleVariableList();
        fSampleGroups = SampleModels.variableGroups();
    }
    
    private VariableDao mockVariableDao()
    {
        final VariableDao dao = mock(VariableDao.class);
        when(dao.getAllVariables()).thenReturn(fSampleVariables);
        // Use a reference to the existing DNR variable so we can do updates.
        final AbstractVariable variable = fSampleVariables.get(3);
        when(dao.getByKey(variable.getKey())).thenReturn(variable);
        when(dao.getAllVariableGroups()).thenReturn(fSampleGroups);
        return dao;
    }
    
    private RiskModelDao mockRiskModelDao()
    {
        final RiskModelDao dao = mock(RiskModelDao.class);
        when(dao.getAllRiskModels()).thenReturn(
                ImmutableList.of(SampleModels.thoracicRiskModel()));
        return dao;
    }
    
    @Test
    public final void testGetAllVariables()
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao());
        
        // Behavior verification.
        // Variables do not override equals() but this works because we use
        // the same Variable objects.
        assertEquals(fSampleVariables, s.getAllVariables());
    }
    
    @Test
    public final void testGetAllVariableGroups()
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao());
        
        // Behavior verification.
        assertEquals(fSampleGroups, s.getAllVariableGroups());
    }
    
    @Test
    public final void testGetVariable() throws Exception
    {
        final String key = "dnr";
        
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao());
        
        // Behavior verification.
        final BooleanVariable actualVar = (BooleanVariable)s.getVariable(key);
        assertEquals(key, actualVar.getKey());
    }
    
    @Test(expected = InvalidIdentifierException.class)
    public final void testGetInvalidVariable() throws Exception
    {
        // Create the class under test.
        final DefaultAdminService s = new DefaultAdminService(
                mockVariableDao(), mockRiskModelDao());
        
        // Behavior verification.
        s.getVariable("Does not exist");
    }
    
    @Test
    public final void testUpdateVariable() throws Exception
    {
    	final String key = "dnr";
        final String origName = "DNR";
        final String newName = "Do Not R";
        final String newHelpText = "Help me!";
        
        // Create the class under test.
        final VariableDao mockDao = mockVariableDao();
        final DefaultAdminService s = new DefaultAdminService(
                mockDao, mockRiskModelDao());
        
        // Setup
        final AbstractVariable var = s.getVariable(key);
        assertEquals(origName, var.getDisplayName());  // sanity check
        
        // Behavior verification.
        var.setDisplayName(newName);
        var.setHelpText(newHelpText);
        s.updateVariable(var);
        
        // Normally we try to verify the contract of a method instead of its
        // implementation, but without Hibernate it is impossible to verify
        // the contract here. Just verify that the service called update().
        verify(mockDao).updateVariable(var);
    }
    
}

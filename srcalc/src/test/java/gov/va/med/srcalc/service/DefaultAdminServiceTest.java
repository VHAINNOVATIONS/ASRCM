package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.Variable;

import org.junit.Test;

public class DefaultAdminServiceTest
{
    private final List<Variable> fSampleVariables;
    
    public DefaultAdminServiceTest()
    {
        fSampleVariables = SampleObjects.sampleVariableList();
    }
    
    public VariableDao mockVariableDao()
    {
        final VariableDao dao = mock(VariableDao.class);
        when(dao.getAllVariables()).thenReturn(fSampleVariables);
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
    
}

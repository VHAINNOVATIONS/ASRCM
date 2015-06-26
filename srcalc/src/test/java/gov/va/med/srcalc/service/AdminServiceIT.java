package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.test.util.IntegrationTest;

import java.util.*;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.google.common.collect.*;

/**
 * Integration Test for {@link AdminService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/srcalc-context.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class AdminServiceIT extends IntegrationTest
{
    @Inject // field-based autowiring only in tests
    AdminService fAdminService;
    
    @Test
    public final void testGetAllVariables()
    {
        final List<AbstractVariable> actualVars = fAdminService.getAllVariables();
        assertEquals(9, actualVars.size());
        assertEquals("Age", actualVars.get(0).getDisplayName());
    }
    
    @Test
    public final void testGetAllVariableGroups()
    {
        final ImmutableCollection<VariableGroup> actualGroups =
                fAdminService.getAllVariableGroups();
        assertEquals(7, actualGroups.size());
        // Sort the collection and examine the first item, which should be the
        // Planned Procedure group, just to make sure the data was actually
        // loaded correctly.
        final VariableGroup plannedProcedure =
                ImmutableSortedSet.copyOf(actualGroups).first();
        assertEquals("Planned Procedure", plannedProcedure.getName());
        assertEquals(0, plannedProcedure.getDisplayOrder());
    }
    
    @Test
    public final void testGetVariable() throws Exception
    {
        final String key = "preopPneumonia";
        final AbstractVariable actualVar = fAdminService.getVariable(key);
        assertThat(actualVar, instanceOf(BooleanVariable.class));
        assertEquals(key, actualVar.getKey());
    }
    
    @Test
    public final void testUpdateVariable() throws Exception
    {
    	final String key = "preopPneumonia";
        final String origName = "Preop Pneumonia";
        final String newName = "Preoperative Issues";
        final String newHelpText = "helpppppppp";
        
        // Setup
        final AbstractVariable var = fAdminService.getVariable(key);
        assertEquals(origName, var.getDisplayName());  // sanity check
        // Sort the groups and pick the 2nd as the new group.
        final ImmutableSortedSet<VariableGroup> sortedGroups =
                ImmutableSortedSet.copyOf(fAdminService.getAllVariableGroups());
        final VariableGroup newGroup = sortedGroups.asList().get(1);
        // Clear the session to simulate a new transaction.
        getHibernateSession().clear();
        
        // Operation
        var.setDisplayName(newName);
        var.setHelpText(Optional.of(newHelpText));
        var.setGroup(newGroup);
        fAdminService.saveVariable(var);
        
        // Verification
        // Simulate a new Hibernate Session.
        getHibernateSession().flush();
        getHibernateSession().clear();
        final AbstractVariable newVar = fAdminService.getVariable(key);
        assertEquals(newName, newVar.getDisplayName());
        assertEquals(newHelpText, newVar.getHelpText().get());
        assertEquals(newGroup, newVar.getGroup());
    }
    
    @Test(expected = DuplicateVariableKeyException.class)
    public final void testSaveDuplicateVariableKey() throws Exception
    {
        final AbstractVariable var = SampleModels.dnrVariable();
        try
        {
            fAdminService.saveVariable(var);
        }
        finally
        {
            // Normally the Session would be dead and gone by now, but in these
            // ITs the Transaction is still open, so manually clear the Session
            // due to the Exception that occurred in the DAO.
            getHibernateSession().clear();
        }
    }
    
    @Test
    public final void testReplaceAllProcedures() throws Exception
    {
        // At time of writing, there are ~10,000 procedures in the real world.
        final int numProcedures = 10000;
        
        final ArrayList<Procedure> newProcedures = new ArrayList<>(numProcedures);
        for (int i = 1; i <= numProcedures; ++i)
        {
            newProcedures.add(new Procedure(
                    String.format("%05d", i),
                    1.0f,
                    "short desc",
                    "long long description",
                    "Complex",
                    true));
        }
        
        fAdminService.replaceAllProcedures(ImmutableSet.copyOf(newProcedures));
        
        // Simulate a new transaction.
        getHibernateSession().flush();
        getHibernateSession().clear();
        
        assertEquals(newProcedures, fAdminService.getAllProcedures());
    }
    
}

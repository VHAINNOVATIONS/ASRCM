package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.test.util.IntegrationTest;

import java.util.List;
import java.util.SortedSet;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
        final SortedSet<VariableGroup> actualGroups = fAdminService.getAllVariableGroups();
        assertEquals(7, actualGroups.size());
        assertEquals("Planned Procedure", actualGroups.first().getName());
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
        final VariableGroup newGroup = fAdminService.getAllVariableGroups().first();
        assertEquals(origName, var.getDisplayName());  // sanity check
        // Clear the session to simulate a new transaction.
        getHibernateSession().clear();
        
        // Operation
        var.setDisplayName(newName);
        var.setHelpText(newHelpText);
        var.setGroup(newGroup);
        fAdminService.updateVariable(var);
        
        // Verification
        // Simulate a new Hibernate Session.
        getHibernateSession().flush();
        getHibernateSession().clear();
        final AbstractVariable newVar = fAdminService.getVariable(key);
        assertEquals(newName, newVar.getDisplayName());
        assertEquals(newHelpText, newVar.getHelpText());
        assertEquals(newGroup, newVar.getGroup());
    }
    
}

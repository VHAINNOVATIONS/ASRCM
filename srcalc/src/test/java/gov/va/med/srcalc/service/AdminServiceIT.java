package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.variable.BooleanVariable;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.test.util.IntegrationTest;

import java.util.List;

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
        final List<Variable> actualVars = fAdminService.getAllVariables();
        assertEquals(8, actualVars.size());
        assertEquals("Age", actualVars.get(0).getDisplayName());
    }
    
    @Test
    public final void testGetVariable() throws Exception
    {
        final BooleanVariable actualVar =
                (BooleanVariable)fAdminService.getVariable("Preop Pneumonia");
        assertEquals("Preop Pneumonia", actualVar.getDisplayName());
    }
    
    @Test
    public final void testUpdateVariable() throws Exception
    {
        final String origName = "Preop Pneumonia";
        final String newName = "Preoperative Issues";
        
        // Setup
        final Variable var = fAdminService.getVariable(origName);
        assertEquals(origName, var.getDisplayName());  // sanity check
        
        // Behavior verification.
        final EditVariable ev = EditVariable.fromVariable(var);
        ev.setDisplayName(newName);
        fAdminService.updateVariable(ev);
        assertEquals(newName, fAdminService.getVariable(newName).getDisplayName());
    }
    
}

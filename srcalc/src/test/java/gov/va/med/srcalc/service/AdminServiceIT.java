package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.variable.BooleanVariable;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.test.util.TestNameLogger;

import java.util.List;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration Test for {@link AdminService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/srcalc-context.xml", "/test-context.xml"})
public class AdminServiceIT
{
    private static final Logger fLogger = LoggerFactory.getLogger(AdminServiceIT.class);
    
    @Inject // field-based autowiring only in tests
    AdminService fAdminService;
    
    @Rule
    public final TestRule fTestLogger = new TestNameLogger(fLogger);
    
    @Test
    @Transactional
    public final void testGetAllVariables()
    {
        final List<Variable> actualVars = fAdminService.getAllVariables();
        assertEquals(8, actualVars.size());
        assertEquals("Age", actualVars.get(0).getDisplayName());
    }
    
    @Test
    @Transactional
    public final void testGetVariable() throws Exception
    {
        final BooleanVariable actualVar =
                (BooleanVariable)fAdminService.getVariable("Preop Pneumonia");
        assertEquals("Preop Pneumonia", actualVar.getDisplayName());
    }
    
    @Test
    @Transactional
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

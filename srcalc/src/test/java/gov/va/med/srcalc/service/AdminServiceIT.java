package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.variable.Variable;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration Test for {@link AdminService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/srcalc-context.xml", "/test-context.xml"})
public class AdminServiceIT
{
    @Inject // field-based autowiring only in tests
    AdminService fAdminService;
    
    @Test
    public final void testGetAllVariables()
    {
        final List<Variable> actualVars = fAdminService.getAllVariables();
        assertEquals(7, actualVars.size());
        assertEquals("Age", actualVars.get(0).getDisplayName());
    }
    
}

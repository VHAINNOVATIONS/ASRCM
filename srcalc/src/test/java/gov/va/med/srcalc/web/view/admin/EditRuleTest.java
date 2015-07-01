package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import gov.va.med.srcalc.db.RiskModelDao;
import gov.va.med.srcalc.db.RuleDao;
import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.service.DefaultAdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;

import java.util.ArrayList;

import org.junit.Test;

public class EditRuleTest
{
    private final DefaultAdminService fAdminService = new DefaultAdminService(
            mock(VariableDao.class), mock(RiskModelDao.class), mock(RuleDao.class));
    
    @Test
    public final void testBasic()
    {
        final EditRule editRule = new EditRule(fAdminService);
        assertEquals(editRule.getDisplayName(), "");
        assertEquals(editRule.getSummandExpression(), "");
        assertFalse(editRule.isBypassEnabled());
        assertEquals(0, editRule.getMatchers().size());
    }
    
    @Test
    public final void testBuildNew() throws InvalidIdentifierException
    {
        final ArrayList<ValueMatcherBuilder> matchers = new ArrayList<ValueMatcherBuilder>();
        final String summandExpression = "summandExpression";
        final boolean required = true;
        final String displayName = "Test Name";
                
        final EditRule editRule = new EditRule(fAdminService);
        editRule.setMatchers(matchers);
        editRule.setSummandExpression(summandExpression);
        editRule.setBypassEnabled(required);
        editRule.setDisplayName(displayName);
        final Rule createdRule = editRule.buildNew();
        
        // Verification
        assertEquals(summandExpression, createdRule.getSummandExpression());
        assertEquals(displayName, createdRule.getDisplayName());
        assertEquals(required, !createdRule.isRequired());
        assertEquals(matchers, createdRule.getMatchers());
    }
}

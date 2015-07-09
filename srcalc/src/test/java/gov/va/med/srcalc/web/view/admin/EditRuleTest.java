package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.va.med.srcalc.db.ProcedureDao;
import gov.va.med.srcalc.db.RiskModelDao;
import gov.va.med.srcalc.db.RuleDao;
import gov.va.med.srcalc.db.VariableDao;
import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.service.DefaultAdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;

import java.util.ArrayList;

import org.junit.Test;

public class EditRuleTest
{
    private final DefaultAdminService fAdminService = new DefaultAdminService(
            mockVariableDao(), 
            mock(RiskModelDao.class),
            mock(RuleDao.class),
            mock(ProcedureDao.class));
    
    private VariableDao mockVariableDao()
    {
        final VariableDao dao = mock(VariableDao.class);
        final AbstractVariable variable = SampleModels.ageVariable();
        when(dao.getByKey(variable.getKey())).thenReturn(variable);
        return dao;
    }
    
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
        final ValueMatcherBuilder builtMatcher = new ValueMatcherBuilder("age");
        builtMatcher.setBooleanExpression("true");
        matchers.add(builtMatcher);
        final String summandExpression = "summandExpression";
        final boolean bypassEnabled = true;
        final String displayName = "Test Name";
                
        final EditRule editRule = new EditRule(fAdminService);
        editRule.setSummandExpression(summandExpression);
        editRule.getMatchers().add(builtMatcher);
        editRule.setBypassEnabled(bypassEnabled);
        editRule.setDisplayName(displayName);
        final Rule createdRule = editRule.buildNew();
        
        // Verification
        assertEquals(summandExpression, createdRule.getSummandExpression());
        assertEquals(displayName, createdRule.getDisplayName());
        assertEquals(bypassEnabled, !createdRule.isBypassEnabled());
        assertEquals(matchers.get(0).buildNew(fAdminService), createdRule.getMatchers().get(0));
    }
}

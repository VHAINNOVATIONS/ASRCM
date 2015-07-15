package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.service.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Tests the {@link EditRule} class.
 */
public class EditRuleTest
{
    private final ModelInspectionService fModelService = new MockModelService();
    
    @Test
    public final void testBasic()
    {
        final EditRule editRule = new EditRule();
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
                
        final EditRule editRule = new EditRule();
        editRule.setSummandExpression(summandExpression);
        editRule.getMatchers().add(builtMatcher);
        editRule.setBypassEnabled(bypassEnabled);
        editRule.setDisplayName(displayName);
        final Rule createdRule = editRule.buildNew(fModelService);
        
        // Verification
        assertEquals(summandExpression, createdRule.getSummandExpression());
        assertEquals(displayName, createdRule.getDisplayName());
        assertEquals(bypassEnabled, createdRule.isBypassEnabled());
        assertEquals(matchers.get(0).buildNew(fModelService), createdRule.getMatchers().get(0));
    }
}

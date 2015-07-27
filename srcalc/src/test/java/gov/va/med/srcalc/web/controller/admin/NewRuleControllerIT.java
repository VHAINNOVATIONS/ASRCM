package gov.va.med.srcalc.web.controller.admin;

import static org.hamcrest.CoreMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.admin.EditRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


/**
 * Tests the NewRuleController.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class NewRuleControllerIT extends IntegrationTest
{
    @Autowired
    WebApplicationContext fWac;
    
    @Autowired
    AdminService fAdminService;
    
    private MockMvc fMockMvc;
    
    private static final String DISPLAY_NAME = "Test Rule";
    private static final String INVALID_EXPRESSION = "true asdf 1234 %^^#%";
    
    @Before
    public void setup()
    {
        fMockMvc = MockMvcBuilders.webAppContextSetup(fWac).build();
    }
    
    @Test
    public final void testNewRuleValid() throws Exception
    {
        fMockMvc.perform(get(NewRuleController.BASE_URL))
            .andExpect(status().isOk())
            .andExpect(model().attribute(
                    BaseRuleController.ATTRIBUTE_RULE, isA(EditRule.class)));
        
        fMockMvc.perform(post(NewRuleController.BASE_URL)
                .param("displayName", DISPLAY_NAME)
                .param("required", "true")
                .param("summandExpression", "#coefficient")
                .param("newVariableKey", "")
                .param("matchers[0].enabled", "true")
                .param("matchers[0].variableKey", "age")
                .param("matchers[0].booleanExpression", "true")
                .param("submitButton", "submit"))
            .andExpect(status().isMovedTemporarily())
            .andExpect(redirectedUrl(SrcalcUrls.MODEL_ADMIN_HOME));
        fAdminService.getRule(DISPLAY_NAME);
    }
    
    @Test
    public final void testNewRuleNoSummand() throws Exception
    {
        fMockMvc.perform(post(NewRuleController.BASE_URL)
                .param("displayName", DISPLAY_NAME)
                .param("required", "true")
                .param("newVariableKey", "")
                .param("submitButton", "submit"))
            .andExpect(model().attributeHasErrors(BaseRuleController.ATTRIBUTE_RULE));
    }
    
    @Test
    public final void testNewRuleInvalidSummand() throws Exception
    {
        fMockMvc.perform(post(NewRuleController.BASE_URL)
                .param("displayName", DISPLAY_NAME)
                .param("required", "true")
                .param("summandExpression", INVALID_EXPRESSION)
                .param("newVariableKey", "")
                .param("matchers[0].enabled", "true")
                .param("matchers[0].variableKey", "age")
                .param("matchers[0].booleanExpression", "true")
                .param("submitButton", "submit"))
            .andExpect(model().attributeHasErrors(BaseRuleController.ATTRIBUTE_RULE));
    }
    
    @Test
    public final void testNewRuleInvalidMatcher() throws Exception
    {
        fMockMvc.perform(post(NewRuleController.BASE_URL)
                .param("displayName", DISPLAY_NAME)
                .param("required", "true")
                .param("summandExpression", "#coefficient")
                .param("newVariableKey", "")
                .param("matchers[0].enabled", "true")
                .param("matchers[0].variableKey", "age")
                .param("matchers[0].booleanExpression", INVALID_EXPRESSION)
                .param("submitButton", "submit"))
            .andExpect(model().attributeHasErrors(BaseRuleController.ATTRIBUTE_RULE));
    }
}

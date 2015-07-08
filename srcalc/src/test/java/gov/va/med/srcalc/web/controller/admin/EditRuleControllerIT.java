package gov.va.med.srcalc.web.controller.admin;

import static org.hamcrest.CoreMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.web.view.admin.EditExistingRule;

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
 * Tests the EditRuleController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class EditRuleControllerIT extends IntegrationTest
{
    @Autowired
    WebApplicationContext fWac;
    
    @Autowired
    AdminService fAdminService;
    
    private MockMvc fMockMvc;

    private static final String INVALID_EXPRESSION = "true asdf 1234 %^^#%";

    @Before
    public void setup()
    {
        fMockMvc = MockMvcBuilders.webAppContextSetup(fWac).build();
    }
    
    @Test
    public final void testEditRuleValid() throws Exception
    {
        fMockMvc.perform(get(EditRuleController.BASE_URL, 1))
            .andExpect(status().isOk())
            .andExpect(model().attribute("rule", isA(EditExistingRule.class)));

        fMockMvc.perform(post(EditRuleController.BASE_URL, 1)
                .param("displayName", "Test Rule")
                .param("summandExpression", "#coefficient")
                .param("submitButton", "")
                .param("matchers[0].enabled", "true")
                .param("matchers[0].variableKey", "age")
                .param("matchers[0].booleanExpression", "true")
                .param("submitButton", ""))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(AdminHomeController.BASE_URL));
        fAdminService.getRuleById(1);
    }
    
    @Test
    public final void testEditRuleInvalidSummand() throws Exception
    {
        fMockMvc.perform(post(EditRuleController.BASE_URL, 1)
                .param("displayName", "TestRule")
                .param("summandExpression", INVALID_EXPRESSION)
                .param("submitButton", "submit"))
            .andExpect(model().attributeErrorCount(NewRuleController.ATTRIBUTE_RULE, 1));
    }
    
    @Test
    public final void testEditRuleInvalidMatcher() throws Exception
    {
        fMockMvc.perform(post(EditRuleController.BASE_URL, 1)
                .param("displayName", "TestRule")
                .param("summandExpression", INVALID_EXPRESSION)
                .param("required", "true")
                .param("matchers[0].enabled", "true")
                .param("matchers[0].variableKey", "age")
                .param("matchers[0].booleanExpression", INVALID_EXPRESSION)
                .param("submitButton", "submit"))
            .andExpect(model().attributeErrorCount(NewRuleController.ATTRIBUTE_RULE, 2));
    }
}

package gov.va.med.srcalc.web.controller.admin;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.web.controller.AdminHomeController;

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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class NewBooleanVarControllerIT extends IntegrationTest
{
    @Autowired
    WebApplicationContext fWac;
    
    @Autowired
    AdminService fAdminService;
    
    private MockMvc fMockMvc;
    
    @Before
    public void setup()
    {
        fMockMvc = MockMvcBuilders.webAppContextSetup(fWac).build();
    }

    @Test
    public final void testNewVariableValid() throws Exception
    {
        final String key = "testNewVariableValidKey";

        fMockMvc.perform(get(NewBooleanVarController.BASE_URL))
            .andExpect(status().isOk())
            .andExpect(model().attribute("variable", hasProperty("key")));
        
        fMockMvc.perform(post(NewBooleanVarController.BASE_URL)
                .param("key", key)
                .param("displayName", "myDisplayName")
                .param("helpText", "myHelpText")
                .param("groupId", "1"))
            .andExpect(redirectedUrl(AdminHomeController.BASE_URL));
        
        // Verify that the variable was actually created. Individual properties
        // are tested in EditBooleanVariableTest.
        assertEquals(key, fAdminService.getVariable(key).getKey());
    }
    
    @Test
    public final void testNewVariableNoDisplayName() throws Exception
    {
        fMockMvc.perform(post(NewBooleanVarController.BASE_URL)
                .param("key", "newKey")
                .param("helpText", "myHelpText")
                .param("groupId", "1"))
            .andExpect(model().attributeHasErrors(NewVarController.ATTRIBUTE_VARIABLE));
    }
}

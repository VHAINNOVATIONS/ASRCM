package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.web.view.Views;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class EditVariableControllerIT extends IntegrationTest
{
    @Autowired
    WebApplicationContext fWac;

    private MockMvc fMockMvc;

    @Before
    public void setup()
    {
        fMockMvc = MockMvcBuilders.webAppContextSetup(fWac).build();
    }
    
    @Test
    public void testEditVariable() throws Exception
    {
        fMockMvc.perform(get("/admin/variables/preopPneumonia")).
            andExpect(status().isOk()).
            andExpect(model().attribute("variable", hasProperty("displayName")));
        
        fMockMvc.perform(
                post("/admin/variables/preopPneumonia").
                param("displayName", "Preop Something")).
            andExpect(redirectedUrl("/admin/models"));
    }
    
    @Test
    public void testEditVariableTooLong() throws Exception
    {
        fMockMvc.perform(get("/admin/variables/preopPneumonia")).
            andExpect(status().isOk()).
            andExpect(model().attribute("variable", hasProperty("displayName")));
        
        fMockMvc.perform(
                post("/admin/variables/preopPneumonia").
                param("displayName",
                        // 81 characters
                        "01234567890123456789012345678901234567890123456789012345678901234567890123456789X")).
            andExpect(model().attributeHasErrors("variable")).
            andExpect(view().name(Views.EDIT_BOOLEAN_VARIABLE));
    }
}

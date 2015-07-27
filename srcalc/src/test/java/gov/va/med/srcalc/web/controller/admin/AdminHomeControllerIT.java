package gov.va.med.srcalc.web.controller.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.web.SrcalcUrls;

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
 * Integration Test for the {@link AdminHomeController}. Note that we only define
 * Integration Tests for controllers because unit tests are of little value.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class AdminHomeControllerIT extends IntegrationTest
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
    public final void testDefaultPage() throws Exception
    {
        fMockMvc.perform(get(SrcalcUrls.ADMIN_HOME))
            .andExpect(status().isOk());
    }
    
    @Test
    public final void testModelHome() throws Exception
    {
        fMockMvc.perform(get(SrcalcUrls.MODEL_ADMIN_HOME))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("variables"))
            .andExpect(model().attributeExists("rules"))
            .andExpect(model().attributeExists("riskModels"));
    }
    
}

package gov.va.med.srcalc.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.web.view.Views;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Integration Test for {@link DefaultController}. Note that we only define
 * Integration Tests for controllers because unit tests are of little value.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class DefaultControllerIT extends IntegrationTest
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
        fMockMvc.perform(get("/"))
            // Test common attributes. These should be in every model but this
            // seems the best place to test it.
            .andExpect(model().attribute(
                    CommonAttributesAdvice.MODEL_ATTRIBUTE_APP_VERSION,
                    // No manifest is available in tests: expect the default
                    CommonAttributesAdvice.DEFAULT_APP_VERSION))
            .andExpect(view().name(Views.LAUNCH_FROM_CPRS));
    }
    
    @Test
    public final void testSessionTimeout() throws Exception
    {
        fMockMvc.perform(get("/sessionTimeout"))
            .andExpect(view().name(Views.SESSION_TIMEOUT));
    }
    
}

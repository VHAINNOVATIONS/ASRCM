package gov.va.med.srcalc.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.web.SrcalcUrls;
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
import org.springframework.transaction.annotation.Transactional;
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
        fMockMvc.perform(get(SrcalcUrls.DEFAULT_PAGE))
            .andExpect(view().name(Views.LAUNCH_FROM_CPRS));
    }

    @Test
    public final void testVistaUserLoginPage() throws Exception
    {
        fMockMvc.perform(get(SrcalcUrls.VISTA_LOGIN_FORM))
            .andExpect(view().name(Views.VISTA_LOGIN_FORM));
    }
    
    @Test
    public final void testSessionTimeout() throws Exception
    {
        fMockMvc.perform(get(SrcalcUrls.SESSION_TIMEOUT_PAGE))
            .andExpect(view().name(Views.SESSION_TIMEOUT));
    }
    
}

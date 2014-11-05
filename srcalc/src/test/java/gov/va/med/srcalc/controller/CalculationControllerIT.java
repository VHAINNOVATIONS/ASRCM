package gov.va.med.srcalc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import gov.va.med.srcalc.test.util.SampleSpecialties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Integration Test for {@link CalculationController}. Note that we only
 * define Integration Tests for controllers because unit tests are of little
 * value.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
public class CalculationControllerIT
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
    public void getSpecialtyList() throws Exception
    {
        fMockMvc.perform(get("/newCalc")).
            andExpect(model().attributeExists("specialties", "calculation"));
    }
    
    @Test
    @Transactional // single transaction for single rollback
    public void selectValidSpecialty() throws Exception
    {
        final String SPECIALTY_NAME = SampleSpecialties.sampleThoracicSpecialty().getName();

        // Share the session between requests.
        final MockHttpSession session = new MockHttpSession();
        
        fMockMvc.perform(get("/newCalc").session(session));

        fMockMvc.perform(post("/selectSpecialty").session(session)
                .param("specialty", SPECIALTY_NAME)).
            andExpect(redirectedUrl("/enterVars"));
    }
}

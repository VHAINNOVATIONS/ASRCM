package gov.va.med.srcalc.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.web.controller.CalculationController;
import static gov.va.med.srcalc.web.view.VariableEntry.makeDynamicValuePath;

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

import static org.hamcrest.Matchers.*;

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
    
    /**
     * The shared HTTP session between requests.
     */
    private MockHttpSession fSession;

    @Before
    public void setup()
    {
        fMockMvc = MockMvcBuilders.webAppContextSetup(fWac).build();
        fSession = new MockHttpSession();
    }
    
    @Test
    public void getSpecialtyList() throws Exception
    {
        fMockMvc.perform(get("/newCalc").session(fSession)).
            andExpect(model().attributeExists("specialties", "calculation"));
    }
    
    /**
     * Test fragment to call {@link #getSpecialtyList()} and then select the
     * given specialty.
     */
    protected void selectSpecialty(final String specialtyName) throws Exception
    {
        getSpecialtyList();

        fMockMvc.perform(post("/selectSpecialty").session(fSession)
                .param("specialty", specialtyName)).
            andExpect(redirectedUrl("/enterVars"));
        
        fMockMvc.perform(get("/enterVars").session(fSession))
            .andExpect(model().attributeExists("calculation"));
    }
    
    @Test
    @Transactional // single transaction for single rollback
    public void selectThoracicSpecialty() throws Exception
    {
        selectSpecialty(SampleObjects.sampleThoracicSpecialty().getName());
    }
    
    @Test
    @Transactional
    public void enterValidThoracicVariables() throws Exception
    {
        selectThoracicSpecialty();
        
        fMockMvc.perform(post("/enterVars").session(fSession)
                // TODO: need a scalable way to specify variables, but just
                // hardcode the parameters for now.
                .param(makeDynamicValuePath("Age"), "55")
                .param(makeDynamicValuePath("Procedure"), "26546"))
            .andExpect(redirectedUrl("/displayResults"));
        
        fMockMvc.perform(get("/displayResults").session(fSession))
            .andExpect(status().is(200))
            .andExpect(model().attribute("calculation", hasProperty("values", hasSize(2))));
        // TODO: validate more model stuff
    }
    
    @Test
    @Transactional
    public void enterInvalidThoracicVariables() throws Exception
    {
        selectThoracicSpecialty();
        
        fMockMvc.perform(post("/enterVars").session(fSession)
                .param(makeDynamicValuePath("Age"), "-2"))
            .andExpect(model().attributeHasErrors("variableEntry"))
            .andExpect(status().is(200));
    }
    
    @Test
    @Transactional
    public void enterValidCardiacVariables() throws Exception
    {
        selectSpecialty("Cardiac");
        
        fMockMvc.perform(post("/enterVars").session(fSession)
                .param(makeDynamicValuePath("Gender"), "Male"))
            .andExpect(redirectedUrl("/displayResults"));
    }
    
    @Test
    @Transactional
    public void enterIncompleteCardiacVariables() throws Exception
    {
        selectSpecialty("Cardiac");
        
        fMockMvc.perform(post("/enterVars").session(fSession))
            // Note no variable parameters specified.
            .andExpect(model().attributeHasErrors("variableEntry"))
            .andExpect(status().is(200));
    }
}

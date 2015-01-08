package gov.va.med.srcalc.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.service.CalculationServiceIT;
import gov.va.med.srcalc.web.controller.CalculationController;
import static gov.va.med.srcalc.web.view.VariableEntry.makeDynamicValuePath;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
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
@Transactional // run each test in its own (rolled-back) transaction
public class CalculationControllerIT extends IntegrationTest
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
            .andExpect(model().attributeExists("calculation", "variableEntry"));
    }
    
    @Test
    public void selectThoracicSpecialty() throws Exception
    {
        selectSpecialty(SampleObjects.sampleThoracicSpecialty().getName());
    }
    
    /**
     * Tests the HTTP interface to enter variables and display the calculation
     * results. Does not test the calculation itself; for that, see
     * {@link CalculationServiceIT#testRunThoracicCalculation()}.
     */
    @Test
    public void enterValidThoracicVariables() throws Exception
    {
        selectThoracicSpecialty();
        
        final DynamicVarParams varParams = new DynamicVarParams();
        varParams.add("Procedure", "26546");
        varParams.add("ASA Classification", "Class 3");
        varParams.add("Age", "55");
        varParams.add("DNR", "false");
        varParams.add("BMI", "18.7");
        varParams.add("Functional Status", "Independent");
        varParams.add("Preop Pneumonia", "true");
        varParams.add("Alkaline Phosphatase", ">125mU/ml");
        
        final MockHttpServletRequestBuilder request =
                post("/enterVars").session(fSession);
        varParams.addTo(request);
        fMockMvc.perform(request)
            .andExpect(model().attributeHasNoErrors("variableEntry"))
            .andExpect(redirectedUrl("/displayResults"));
        
        fMockMvc.perform(get("/displayResults").session(fSession))
            .andExpect(status().is(200))
            // We do not test the calculation itself (see method Javadoc), so
            // just ensure the values list is populated.
            .andExpect(model().attribute(
                    "calculation", hasProperty("values", not(empty()))));
    }
    
    @Test
    public void enterInvalidThoracicVariables() throws Exception
    {
        selectThoracicSpecialty();
        
        fMockMvc.perform(post("/enterVars").session(fSession)
                .param(makeDynamicValuePath("Age"), "-2"))
            .andExpect(model().attributeHasErrors("variableEntry"))
            .andExpect(status().is(200));
    }
    
    @Test
    public void enterValidCardiacVariables() throws Exception
    {
        selectSpecialty("Cardiac");
        
        final DynamicVarParams varParams = new DynamicVarParams();
        varParams.add("Gender", "Male");
        varParams.add("Age", "55");
        varParams.add("BMI", "18.7");
        varParams.add("Functional Status", "Independent");
        
        fMockMvc.perform(
                varParams.addTo(post("/enterVars").session(fSession)))
            .andExpect(redirectedUrl("/displayResults"));
    }
    
    @Test
    public void enterIncompleteCardiacVariables() throws Exception
    {
        selectSpecialty("Cardiac");
        
        fMockMvc.perform(post("/enterVars").session(fSession))
            // Note no variable parameters specified.
            .andExpect(model().attributeHasErrors("variableEntry"))
            .andExpect(status().is(200));
    }
}

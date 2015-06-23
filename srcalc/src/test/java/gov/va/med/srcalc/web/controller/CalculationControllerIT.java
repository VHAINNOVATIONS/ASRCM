package gov.va.med.srcalc.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collection;

import gov.va.med.srcalc.domain.calculation.Calculation;
import gov.va.med.srcalc.domain.calculation.ProcedureValue;
import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.service.CalculationServiceIT;
import gov.va.med.srcalc.vista.VistaPatientDao;
import gov.va.med.srcalc.web.controller.CalculationController;
import gov.va.med.srcalc.web.controller.DisplayResultsController;
import gov.va.med.srcalc.web.view.VariableEntry;
import static gov.va.med.srcalc.web.view.VariableEntry.makeDynamicValuePath;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Integration Test for {@link CalculationController}, {@link DisplayResultsController} and
 * {@link EnterVariablesController}. Note that we only define Integration Tests for
 * controllers because unit tests are of little value.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class CalculationControllerIT extends IntegrationTest
{
    private final int MOCK_DFN = 456123;
    private final static String ELECTRONIC_SIGNATURE = "TESTSIG";
    public final static String NOTE_BODY = "Specialty = Thoracic\n\nCalculation Inputs\n"
    		+ "Age = 45.0\nDNR = No\nFunctional Status = Independent\n"
    		+ "Procedure = 26546 - Repair left hand - you know, the thing with fingers (10.06)\n\n"
    		+ "Results\nThoracic 30-day mortality estimate = 100.0%\n";
   
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
    public void testStartNewCalculationWithDfn() throws Exception
    {
        fMockMvc.perform(get("/newCalc")
                .param("patientDfn", Integer.toString(MOCK_DFN)).session(fSession)).
            andExpect(model().attributeExists("specialties", "calculation"));
    }
    
    @Test
    public void testStartNewCalculationNoDfn() throws Exception
    {
        fMockMvc.perform(get("/newCalc").session(fSession))
            .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void testCalculationInSession() throws Exception
    {
        SrcalcSession.setCalculationSession(fSession, new CalculationSession(new Calculation()));
        fMockMvc.perform(get("/newCalc").session(fSession)
            .param("patientDfn", Integer.toString(MOCK_DFN)).session(fSession))
            .andExpect(model().attributeExists("calculation", "newPatientDfn"));
    }
    
    
    /**
     * Test fragment to call {@link #testStartNewCalculationWithDfn()} and then select the
     * given specialty.
     */
    protected void selectSpecialty(final String specialtyName) throws Exception
    {
        testStartNewCalculationWithDfn();

        fMockMvc.perform(post("/selectSpecialty").session(fSession)
                .param("specialty", specialtyName)).
            andExpect(redirectedUrl("/enterVars"));
        
        fMockMvc.perform(get("/enterVars").session(fSession))
            .andExpect(model().attributeExists("calculation", "variableEntry"));
    }
    
    @Test
    public void selectThoracicSpecialty() throws Exception
    {
        selectSpecialty(SampleModels.thoracicSpecialty().getName());
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
        varParams.add("procedure", "26546");
        varParams.add("asaClassification", "Class 3");
        varParams.add("age", "55");
        varParams.add("dnr", "false");
        varParams.add("bmi", "18.7");
        varParams.add("preopPneumonia", "true");
        varParams.add("alkalinePhosphatase", ">125mU/ml");
        varParams.add("bun", VariableEntry.SPECIAL_NUMERICAL);
        varParams.add(VariableEntry.makeNumericalInputName("bun"), "15.0");
        
        final MockHttpServletRequestBuilder request =
                post("/enterVars").session(fSession);
        varParams.addTo(request);
        fMockMvc.perform(request)
            .andExpect(redirectedUrl("/displayResults"));
        
        final MvcResult result =
            fMockMvc.perform(get("/displayResults").session(fSession))
            .andExpect(status().is(200))
            // We do not test the calculation itself (see method Javadoc), so
            // just ensure the expected objects are in the model.
            .andExpect(model().attributeExists("calculation", "result", "inputValues"))
            .andReturn();
        
        // Check that the ProcedureValue is first.
        final Collection<?> inputValues =
                (Collection<?>)result.getModelAndView().getModel().get("inputValues");
        assertTrue(
                "ProcedureValue should be first",
                inputValues.iterator().next() instanceof ProcedureValue);
    }
    
    @Test
    public void enterInvalidThoracicVariables() throws Exception
    {
        selectThoracicSpecialty();
        
        fMockMvc.perform(post("/enterVars").session(fSession)
                .param(makeDynamicValuePath("age"), "-2"))
            .andExpect(model().attributeHasErrors("variableEntry"))
            .andExpect(status().is(200));
    }
    
    @Test
    public void enterValidCardiacVariables() throws Exception
    {
        selectSpecialty("Cardiac");
        
        final DynamicVarParams varParams = new DynamicVarParams();
        varParams.add("gender", "Male");
        varParams.add("age", "55");
        varParams.add("bmi", "18.7");
        
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
    
    @Test
    public void getProcedures() throws Exception
    {
        fMockMvc.perform(get("/refdata/procedures").accept("application/json"))
            .andExpect(status().is(200))
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", hasSize(3)))
            // Check the first CPT code to ensure correct order.
            .andExpect(jsonPath("$[0].cptCode").value("10001"));
    }
    
    @Test
    public void populateDynamicValues() throws Exception
    {
    	selectThoracicSpecialty();
        
        final DynamicVarParams varParams = new DynamicVarParams();
        varParams.add("procedure", "26546");
        varParams.add("asaClassification", "Class 3");
        varParams.add("age", "55.0");
        varParams.add("dnr", "false");
        varParams.add("bmi", "18.7");
        varParams.add("preopPneumonia", "true");
        varParams.add("alkalinePhosphatase", ">125mU/ml");
        varParams.add("bun", VariableEntry.SPECIAL_NUMERICAL);
        varParams.add(VariableEntry.makeNumericalInputName("bun"), "15.0");
        
        final MockHttpServletRequestBuilder request =
                post("/enterVars").session(fSession);
        varParams.addTo(request);
        fMockMvc.perform(request)
            .andExpect(redirectedUrl("/displayResults"));
        
        // Send a request to the /displayResults page to simulate the workflow
        // even though we don't directly care about the result.
        fMockMvc.perform(get("/displayResults").session(fSession))
            .andExpect(status().is(200));

        final MvcResult result = fMockMvc.perform(get("/enterVars").session(fSession))
        	.andReturn();
        final VariableEntry variableEntry = (VariableEntry) result.getModelAndView().getModel().get("variableEntry");
        assertEquals(varParams.getPairs(), variableEntry.getDynamicValues());
    }
    
    @Test
    public void checkHeaders() throws Exception
    {
    	// Specialty does not matter here
    	testStartNewCalculationWithDfn();

        fMockMvc.perform(post("/selectSpecialty").session(fSession)
            .param("specialty", "Cardiac"))
            .andExpect(redirectedUrl("/enterVars"));
        
        fMockMvc.perform(get("/enterVars").session(fSession))
            .andExpect(header().string("Cache-Control", "no-cache, no-store, must-revalidate"))
            .andExpect(header().string("Pragma", "no-cache"))
            .andExpect(header().string("Expires", "0"));
    }
    
    @Test
    public void enterValidElectronicSignature() throws Exception
    {
    	enterValidCardiacVariables();
        
        fMockMvc.perform(post("/signCalculation").session(fSession)
	        .param("eSig", ELECTRONIC_SIGNATURE))
	        .andExpect(status().isOk())
	        .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
	        		MediaType.APPLICATION_JSON.getSubtype())))
	        .andExpect(jsonPath("$.*", hasSize(1)))
	        .andExpect(jsonPath("$.status", is(VistaPatientDao.SaveNoteCode.SUCCESS.getDescription())));
    }
}

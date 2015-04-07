package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.domain.workflow.CalculationWorkflow;
import gov.va.med.srcalc.vista.VistaPatientDao;
import gov.va.med.srcalc.web.view.Views;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class DisplayResultsController
{
	private static String SESSION_CALCULATION = "srcalc_calculation";
	
	private final VistaPatientDao fPatientDao;
    
    @Inject
    public DisplayResultsController(final VistaPatientDao patientDao)
    {
        fPatientDao = patientDao;
    }
    /**
     * Returns the current CalculationWorkflow object form the HttpSession.
     * @return never null
     * @throws IllegalStateException if there is no current calculation
     */
    public static CalculationWorkflow getWorkflowFromSession(final HttpSession session)
    {
        final CalculationWorkflow workflow =
                (CalculationWorkflow)session.getAttribute(SESSION_CALCULATION);
        if (workflow == null)
        {
            throw new IllegalStateException("No current calculation.");
        }
        return workflow;
    }
    
    @RequestMapping(value = "/displayResults", method = RequestMethod.GET)
    public String displayResults(
            final HttpSession session,
            final Model model)
    {
        // Get the CalculationWorkflow from the session.
        final CalculationWorkflow workflow = getWorkflowFromSession(session);
        
        model.addAttribute("calculation", workflow.getCalculation());
        return Views.DISPLAY_RESULTS;
    }
    
    @RequestMapping(
    		value="/displayResults",
    		method = RequestMethod.POST,
    		produces = "application/json")
    @ResponseBody
    public String signCalculation(final HttpSession session, final Model model)
    {
    	// Do a normal post with ajax jquery javascript
    	// Use a JSON object to receive the information from VistA on failure/success
    	// Make a business call in one line to the domain/business area
    	// Web code can be as large as needed
//    	final String resultString = fPatientDao.saveRiskCalculationNote(
//    			getWorkflowFromSession(session).getCalculation(),
//    			electronicSignature,
//    			noteBody)
    	// Call the RPC that we need to and await the response
    	
    	// Return the result that will be translated to a json response
    	return "";
    }
}

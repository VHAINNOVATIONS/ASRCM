package gov.va.med.srcalc.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gov.va.med.srcalc.domain.workflow.CalculationWorkflow;
import gov.va.med.srcalc.vista.VistaPatientDao;
import gov.va.med.srcalc.web.view.Views;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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
    public List<Object> signCalculation(final HttpSession session, 
    		@RequestParam(value = "eSig") final String electronicSignature,
    		final Model model)
    {
    	// Build the note body and submit the RPC
    	final String resultString = fPatientDao.saveRiskCalculationNote(
    			getWorkflowFromSession(session).getCalculation(),
    			electronicSignature);
    	// The json could be expanded to return more information/fields
    	final List<Object> returnList = new ArrayList<>();
        final HashMap<String, String> jsonStatus = new HashMap<>();
        jsonStatus.put("status", resultString);
        returnList.add(jsonStatus);
    	// Return the result that will be translated to a json response
    	return returnList;
    }
    
    @RequestMapping(value="/successfulSign", method = RequestMethod.GET)
    public String displaySuccess(final HttpSession session, final Model model)
    {
    	return Views.SUCCESSFUL_SIGN;
    }
}

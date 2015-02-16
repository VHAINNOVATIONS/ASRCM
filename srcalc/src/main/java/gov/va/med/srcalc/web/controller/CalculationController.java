package gov.va.med.srcalc.web.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import gov.va.med.srcalc.domain.workflow.CalculationWorkflow;
import gov.va.med.srcalc.domain.workflow.NewCalculation;
import gov.va.med.srcalc.domain.workflow.SelectedCalculation;
import gov.va.med.srcalc.service.CalculationService;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.web.view.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for creating a new Calculation.
 */
@Controller
public class CalculationController
{
    private static String SESSION_PATIENT_DFN = "srcalc_patientDfn";
    private static String SESSION_CALCULATION = "srcalc_calculation";
    
    private final CalculationService fCalculationService;
    
    @Inject
    public CalculationController(final CalculationService calculationService)
    {
        fCalculationService = calculationService;
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
    
    /**
     * Encapsulates the following logic to determine which patient DFN to use:
     * <ol>
     * <li>If a DFN was provided in the request, use it.</li>
     * <li>Otherwise, if there is already a DFN in the session, use it.</li>
     * <li>Otherwise, abort the calculation.</li>
     * </ol>
     * @param providedDfn the patient DFN for the calculation. Only required if
     * there is not already a current DFN in the Session. -1 represents no value
     * @param session the HttpSession
     * @return the patient DFN to use
     * @throws IllegalArgumentException if we hit #3 above
     */
    private int determinePatientDfn(
            final HttpSession session, final int providedDfn)
    {
        // If a DFN was provided in the request, use it.
        if (providedDfn != -1)
        {
            // Store the DFN in the session for the next new calculation.
            session.setAttribute(SESSION_PATIENT_DFN, providedDfn);
            
            return providedDfn;
        }
        // Otherwise, if there is already a DFN in the session, use it.
        else
        {
            // Use the boxed type (Integer) since the Session attribute may be null.
            final Integer sessionDfn = (Integer)session.getAttribute(SESSION_PATIENT_DFN);
            // If there was no DFN provided or in the session, abort.
            if (sessionDfn == null)
            {
                throw new IllegalArgumentException("A patient DFN is required.");
            }

            return sessionDfn;
        }
    }

    /**
     * Starts a new calculation.
     * @param providedDfn the patient DFN for the calculation. Only required if
     * there is not already a current DFN in the Session. -1 represents no value
     * @param session the HttpSession
     * @return the {@link ModelAndView} for continuing the calculation
     */
    @RequestMapping(value = "/newCalc", method = RequestMethod.GET)
    public ModelAndView startNewCalculation(
            @RequestParam(value = "patientDfn", defaultValue = "-1") final int providedDfn,
            final HttpSession session)
    {
        final int patientDfn = determinePatientDfn(session, providedDfn);

        // Start the calculation. A Calculation object must be created here to
        // store the start time for reporting.
        final NewCalculation newCalc = fCalculationService.startNewCalculation(patientDfn);

        // Store the calculation in the HTTP Session.
        session.setAttribute(SESSION_CALCULATION, newCalc);
        
        // Present the view.
        final ModelAndView mav = new ModelAndView(Tile.SELECT_SPECIALTY);
        mav.addObject("calculation", newCalc.getCalculation());
        mav.addObject("specialties", newCalc.getPossibleSpecialties());
        return mav;
    }
    
    @RequestMapping(value = "/selectSpecialty", method = RequestMethod.POST)
    public String setSpecialty(
            final HttpSession session,
            @RequestParam("specialty") final String specialtyName)
                    throws InvalidIdentifierException
    {
        final CalculationWorkflow workflow = getWorkflowFromSession(session);
        final SelectedCalculation selCalc =
                fCalculationService.setSpecialty(workflow.getCalculation(), specialtyName);
        session.setAttribute(SESSION_CALCULATION, selCalc);

        // Using the POST-redirect-GET pattern.
        return "redirect:/enterVars";
    }
    
    // Variable entry is in EnterVariablesController.
    
    @RequestMapping(value = "/displayResults", method = RequestMethod.GET)
    public String displayResults(
            final HttpSession session,
            final Model model)
    {
        // Get the CalculationWorkflow from the session.
        final CalculationWorkflow workflow = getWorkflowFromSession(session);
        
        model.addAttribute("calculation", workflow.getCalculation());
        return Tile.DISPLAY_RESULTS;
    }
}

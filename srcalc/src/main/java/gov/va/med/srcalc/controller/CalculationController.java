package gov.va.med.srcalc.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.workflow.NewCalculation;
import gov.va.med.srcalc.service.CalculationService;
import gov.va.med.srcalc.service.InvalidIdentifierException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for creating a new Calculation.
 */
@Controller
public class CalculationController
{
    private static String SESSION_CALCULATION = "srcalc_calculation";
    
    private final CalculationService fCalculationService;
    
    @Inject
    public CalculationController(final CalculationService calculationService)
    {
        fCalculationService = calculationService;
    }

    @RequestMapping(value = "/newCalc", method = RequestMethod.GET)
    public String presentSelection(HttpSession session, final Model model)
    {
        // A Calculation object must be created here to store the start time for
        // the "Time To Completion" report.
        
        // FIXME: fake
        final int FAKE_PATIENT_DFN = 1;

        // Start the calculation.
        final NewCalculation newCalc = fCalculationService.startNewCalculation(FAKE_PATIENT_DFN);

        // Store the calculation in the HTTP Session.
        session.setAttribute(SESSION_CALCULATION, newCalc.getCalculation());
        
        // Present the view.
        model.addAttribute("calculation", newCalc.getCalculation());
	model.addAttribute("specialties", newCalc.getPossibleSpecialties());
	return "srcalc.selectSpecialty";
    }
    
    @RequestMapping(value = "/selectSpecialty", method = RequestMethod.POST)
    public String setSpecialty(
            HttpSession session,
            @RequestParam("specialty") final String specialtyName)
                    throws InvalidIdentifierException
    {
        final Calculation calc =
                (Calculation)session.getAttribute(SESSION_CALCULATION);
        fCalculationService.setSpecialty(calc, specialtyName);

        // Using the POST-redirect-GET pattern.
        return "redirect:/enterVars";
    }
    
    @RequestMapping(value = "/enterVars", method = RequestMethod.GET)
    public String presentVariableEntry(
            HttpSession session,
            final Model model)
    {
        final Calculation calc =
                (Calculation)session.getAttribute(SESSION_CALCULATION);

        // Present the view.
        model.addAttribute("calculation", calc);
        return "srcalc.enterVariables";
    }
}

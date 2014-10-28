package gov.va.med.srcalc.controller;

import javax.inject.Inject;

import gov.va.med.srcalc.domain.workflow.NewCalculation;
import gov.va.med.srcalc.service.CalculationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for creating a new Calculation.
 */
@Controller
public class NewCalculationController
{
    private final CalculationService fCalculationService;
    
    @Inject
    public NewCalculationController(final CalculationService calculationService)
    {
        fCalculationService = calculationService;
    }

    @RequestMapping(value = "/newCalc", method = RequestMethod.GET)
    public String presentSelection(final Model model)
    {
        // A Calculation object must be created here to store the start time for
        // the "Time To Completion" report.
        
        // FIXME: fake
        final int FAKE_PATIENT_DFN = 1;

        final NewCalculation newCalc = fCalculationService.startNewCalculation(FAKE_PATIENT_DFN);
        model.addAttribute("calculation", newCalc.getCalculation());
	model.addAttribute("specialties", newCalc.getPossibleSpecialties());
	// View name
	return "/selectSpecialty";
    }
}

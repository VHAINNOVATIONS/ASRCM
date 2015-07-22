package gov.va.med.srcalc.web.controller;

import java.util.HashMap;

import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.service.CalculationService;
import gov.va.med.srcalc.web.view.ValueDisplayOrder;
import gov.va.med.srcalc.web.view.Views;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.ImmutableSortedSet;

/**
 * Web MVC controller to display calculation results and allow the user to sign them.
 */
@Controller
public class DisplayResultsController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayResultsController.class);
    private final CalculationService fCalculationService;
    
    @Inject
    public DisplayResultsController(final CalculationService calculationService)
    {
        fCalculationService = calculationService;
    }
    
    @RequestMapping(value = "/displayResults", method = RequestMethod.GET)
    public String displayResults(
            final HttpSession session,
            final Model model)
    {
        // Get the current CalculationSession.
        final CalculationSession cs = SrcalcSession.getCalculationSession(session);
        model.addAttribute("calculation", cs.getCalculation());

        // And get the current CalculationResult from the session.
        final CalculationResult result = cs.getRequiredLastResult();
        model.addAttribute("result", result);
        // Sort the input values in the desired display order and add to the
        // model.
        final ValueDisplayOrder displayOrder = new ValueDisplayOrder();
        model.addAttribute("inputValues",
                ImmutableSortedSet.copyOf(displayOrder, result.getValues()));
        
        return Views.DISPLAY_RESULTS;
    }
    
    @RequestMapping(
            value="/signCalculation",
            method = RequestMethod.POST,
            produces = "application/json")
    @ResponseBody
    public HashMap<String, String> signCalculation(final HttpSession session, 
            @RequestParam(value = "eSig") final String electronicSignature,
            final Model model)
    {
        // Build the note body and submit the RPC
        String resultString;
        try
        {
            final CalculationResult lastResult =
                    SrcalcSession.getCalculationSession(session).getRequiredLastResult();
            resultString = fCalculationService.signRiskCalculation(
                    lastResult, electronicSignature).getDescription();
        }
        catch(final RecoverableDataAccessException e)
        {
            LOGGER.warn("There was a problem connecting to VistA", e);
            resultString = "There was a problem connecting to VistA.";
        }
        // The json could be expanded to return more information/fields
        final HashMap<String, String> jsonStatus = new HashMap<>();
        jsonStatus.put("status", resultString);
        // Return the result that will be translated to a json response
        return jsonStatus;
    }
    
    @RequestMapping(value="/successfulSign", method = RequestMethod.GET)
    public String displaySuccess(final HttpSession session, final Model model)
    {
        return Views.SUCCESSFUL_SIGN;
    }
}

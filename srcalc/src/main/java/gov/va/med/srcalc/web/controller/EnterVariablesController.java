package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.domain.variable.MissingValueException;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.domain.workflow.CalculationWorkflow;
import gov.va.med.srcalc.service.CalculationService;
import gov.va.med.srcalc.util.MissingValueListException;
import gov.va.med.srcalc.web.view.*;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * A "branch" of {@link CalculationController} containing request handlers
 * specifically for variable entry.
 */
@Controller
public class EnterVariablesController
{
    private static final Logger fLogger = LoggerFactory.getLogger(CalculationController.class);

    /**
     * Attribute name for the VariableEntry object.
     */
    public static final String ATTR_VARIABLE_ENTRY = "variableEntry";
    
    private final CalculationService fCalculationService;
    
    @Inject
    public EnterVariablesController(final CalculationService calculationService)
    {
        fCalculationService = calculationService;
    }
    
    @ModelAttribute
    public VariableEntry constructVariableEntry(
            final HttpSession session)
    {
        // Get the CalculationWorkflow from the session.
        final CalculationWorkflow workflow =
                CalculationController.getWorkflowFromSession(session);

        return new VariableEntry(workflow.getCalculation().getVariables());
    }
    
    /**
     * Presents that variable entry form.
     * @param session the current HTTP session (required)
     * @param initialValues the initial values to set
     * @return a ModelAndView for view rendering
     */
    @RequestMapping(value = "/enterVars", method = RequestMethod.GET)
    public ModelAndView presentVariableEntry(
            final HttpSession session,
            @ModelAttribute(ATTR_VARIABLE_ENTRY) final VariableEntry initialValues)
    {
        // Get the CalculationWorkflow from the session.
        final CalculationWorkflow workflow =
                CalculationController.getWorkflowFromSession(session);

        // Present the view.
        final ModelAndView mav = new ModelAndView(Tile.ENTER_VARIABLES);
        mav.addObject("calculation", workflow.getCalculation());
        // Note: "variableEntry" object is automatically added through annotated
        // method parameter.
        return mav;
    }
    
    @RequestMapping(value = "/enterVars", method = RequestMethod.POST)
    public ModelAndView enterVariables(
            final HttpSession session,
            @ModelAttribute(ATTR_VARIABLE_ENTRY) final VariableEntry values,
            final BindingResult valuesBindingResult)
    {
        // Get the CalculationWorkflow from the session.
        final CalculationWorkflow workflow =
                CalculationController.getWorkflowFromSession(session);
        
        // Extract the values from the HTTP POST.
        final InputParserVisitor parserVisitor = new InputParserVisitor(values, valuesBindingResult);
        for (final Variable variable : workflow.getCalculation().getVariables())
        {
            parserVisitor.visit(variable);
        }
        
        try
        {
        	// Set the values on the Calculation.
        	fCalculationService.runCalculation(
        			workflow.getCalculation(), parserVisitor.getValues());
        }
        catch(final MissingValueListException e)
        {
        	// Add all of the missing values to the binding errors
        	for(final MissingValueException missingValue : e.getMissingValues())
        	{
        		// If the variable had invalid input, rather than missing input,
        		// it would never be included in the values.
        		fLogger.debug("Field Error: {}", VariableEntry.makeDynamicValuePath(missingValue.getVariable().getKey()));
        		final String dynamicKey = VariableEntry.makeDynamicValuePath(missingValue.getVariable().getKey());
        		// Account for field errors on special fields like discrete numerical
        		// variables.
        		final String numericalKey = VariableEntry.makeDynamicValuePath(
        				missingValue.getVariable().getKey() + VariableEntry.SEPARATOR + VariableEntry.SPECIAL_NUMERICAL);
        		if(!valuesBindingResult.hasFieldErrors(dynamicKey) &&
        				!valuesBindingResult.hasFieldErrors(numericalKey))
        		{
        			valuesBindingResult.rejectValue(
        					dynamicKey,
        	                missingValue.getCode(),
        	                missingValue.getMessage());
        		}
        	}
        }
        
        // If we have any binding errors, return to the Enter Variables screen.
        if (valuesBindingResult.hasErrors())
        {
            fLogger.debug(
                    "Invalid variables entered by user. Reshowing variable entry screen. Errors: {}",
                    valuesBindingResult.getAllErrors());
            return presentVariableEntry(session, values);
        }

        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:/displayResults");
    }
}

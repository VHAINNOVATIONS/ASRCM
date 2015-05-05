package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.workflow.CalculationWorkflow;
import gov.va.med.srcalc.service.CalculationService;
import gov.va.med.srcalc.util.MissingValuesException;
import gov.va.med.srcalc.web.DynamicValueVisitor;
import gov.va.med.srcalc.web.view.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
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
    private static final Logger fLogger = LoggerFactory.getLogger(EnterVariablesController.class);

    /**
     * Attribute name for the VariableEntry object.
     */
    public static final String ATTR_VARIABLE_ENTRY = "variableEntry";
    
    /**
     * Error code used when a required value is not provided.
     */
    public static final String ERROR_NO_VALUE = "noInput";
    
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
        		CalculationWorkflowSupplier.getWorkflowFromSession(session);
        final VariableEntry initialValues = VariableEntry.withRetrievedValues(workflow.getCalculation().getVariables(), 
        		workflow.getCalculation().getPatient());
        // In the case of using the "Return to Input Form" button, add the values already
        // in the calculation to the variable entry object to maintain the previously calculated
        // values on the entry page.
        final DynamicValueVisitor visitor = new DynamicValueVisitor(initialValues);
        for(final Value value: workflow.getCalculation().getValues())
        {
        	visitor.visit(value);
        	fLogger.debug("Key: {} Value: {}", value.getVariable().getKey(), value.getValue());
        }
        return initialValues;
    }
    
    /**
     * Presents that variable entry form.
     * @param session the current HTTP session (required)
     * @param response needed to alter the response header to expire the page
     * 		after a completed calculation
     * @param initialValues the initial values to set
     * @return a ModelAndView for view rendering
     */
    @RequestMapping(value = "/enterVars", method = RequestMethod.GET)
    public ModelAndView presentVariableEntry(
            final HttpSession session, final HttpServletResponse response,
            @ModelAttribute(ATTR_VARIABLE_ENTRY) final VariableEntry initialValues)
    {
    	// Expire the page to warn the user that a back button is not viable
    	// after completing a calculation. The page will require a reload if the back button
    	// is used to go back to the enter variables page.
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    	response.setHeader("Pragma", "no-cache");
    	response.setDateHeader("Expires", 0);
        // Get the CalculationWorkflow from the session.
        final CalculationWorkflow workflow =
        		CalculationWorkflowSupplier.getWorkflowFromSession(session);

        // Present the view.
        final ModelAndView mav = new ModelAndView(Views.ENTER_VARIABLES);
        mav.addObject("calculation", workflow.getCalculation());
        
        fLogger.debug("Input Values: {}", workflow.getCalculation().getValues());
        // Note: "variableEntry" object is automatically added through annotated
        // method parameter.
        return mav;
    }
    
    @RequestMapping(value = "/enterVars", method = RequestMethod.POST)
    public ModelAndView enterVariables(
            final HttpSession session, final HttpServletResponse response,
            @ModelAttribute(ATTR_VARIABLE_ENTRY) final VariableEntry values,
            final BindingResult valuesBindingResult)
    {
        // Get the CalculationWorkflow from the session.
        final CalculationWorkflow workflow =
        		CalculationWorkflowSupplier.getWorkflowFromSession(session);
        
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
        catch(final MissingValuesException e)
        {
        	// Add all of the missing values to the binding errors
        	for(final MissingValueException missingValue : e.getMissingValues())
        	{
        		// If the variable had invalid input, rather than missing input,
        		// it would never be included in the values.
        		final String dynamicKey = VariableEntry.makeDynamicValuePath(missingValue.getVariable().getKey());
        		fLogger.debug("Field Error: {}", dynamicKey);
        		// Account for field errors on special fields like discrete numerical
        		// variables.
        		if(bindingResultAlreadyContainsError(dynamicKey, valuesBindingResult, missingValue))
        		{
                            valuesBindingResult.rejectValue(
                                    dynamicKey, ERROR_NO_VALUE, missingValue.getMessage());
        		}
        	}
        }
        
        // If we have any binding errors, return to the Enter Variables screen.
        if (valuesBindingResult.hasErrors())
        {
            fLogger.debug(
                    "Invalid variables entered by user. Reshowing variable entry screen. Errors: {}",
                    valuesBindingResult.getAllErrors());
            return presentVariableEntry(session, response, values);
        }

        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:/displayResults");
    }
    
    private boolean bindingResultAlreadyContainsError(final String dynamicKey,
    		final BindingResult valuesBindingResult, final MissingValueException missingValue)
    {
    	final String numericalKey = VariableEntry.makeDynamicValuePath(
				missingValue.getVariable().getKey() + VariableEntry.SEPARATOR + VariableEntry.SPECIAL_NUMERICAL);
		return !valuesBindingResult.hasFieldErrors(dynamicKey) &&
				!valuesBindingResult.hasFieldErrors(numericalKey);
    }
}

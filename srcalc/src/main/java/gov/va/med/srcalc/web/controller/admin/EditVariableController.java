package gov.va.med.srcalc.web.controller.admin;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.web.view.admin.*;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Web MVC controller for editing existing variables.
 */
@Controller
@RequestMapping("/admin/variables/{variableKey}")
public class EditVariableController
{
    private static final String ATTRIBUTE_VARIABLE = "variable";
    
    private final AdminService fAdminService;
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     */
    @Inject
    public EditVariableController(final AdminService adminService)
    {
        fAdminService = adminService;
    }
    
    /**
     * Initializes the given WebDataBinder with the necessary validators.
     */
    @InitBinder
    protected void initBinder(final WebDataBinder binder)
    {
        binder.addValidators(new EditVariableValidator());
    }
    
    /**
     * Creates an {@link EditExistingVariable} instance for the identified
     * variable.
     * @param variableKey the name of the variable to edit
     * @return the EditExistingVariable instance
     * @throws InvalidIdentifierException if no such variable exists
     */
    @ModelAttribute(ATTRIBUTE_VARIABLE)
    public EditExistingVariable createEditVariable(
            @PathVariable final String variableKey)
            throws InvalidIdentifierException
    {
        final AbstractVariable var = fAdminService.getVariable(variableKey);
        return EditVariableFactory.getInstance(var, fAdminService);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm(
            @ModelAttribute(ATTRIBUTE_VARIABLE) final EditExistingVariable editVariable)
            throws InvalidIdentifierException
    {
        final ModelAndView mav = new ModelAndView(editVariable.getEditViewName());
        // Note: "variable" is in the model via createEditVariable() above.
        
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveVariable(
            @ModelAttribute(ATTRIBUTE_VARIABLE) @Valid final EditExistingVariable editVariable,
            final BindingResult bindingResult)
                    throws InvalidIdentifierException
    {
        if (bindingResult.hasErrors())
        {
            // Re-show the edit screen.
            return displayForm(editVariable);
        }
        
        // Apply the changes to the persistent variable.
        fAdminService.saveVariable(editVariable.applyToVariable());

        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:/admin");
    }
    
}

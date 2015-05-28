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
        binder.addValidators(new EditVarValidator());
    }
    
    /**
     * Creates an {@link EditExistingVar} instance for the identified
     * variable.
     * @param variableKey the name of the variable to edit
     * @return the EditExistingVar instance
     * @throws InvalidIdentifierException if no such variable exists
     */
    @ModelAttribute(ATTRIBUTE_VARIABLE)
    public EditExistingVar createEditVar(
            @PathVariable final String variableKey)
            throws InvalidIdentifierException
    {
        final AbstractVariable var = fAdminService.getVariable(variableKey);
        return EditVarFactory.getInstance(var, fAdminService);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm(
            @ModelAttribute(ATTRIBUTE_VARIABLE) final EditExistingVar editVar)
            throws InvalidIdentifierException
    {
        final ModelAndView mav = new ModelAndView(editVar.getEditViewName());
        // Note: "variable" is in the model via createEditVar() above.
        
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveVariable(
            @ModelAttribute(ATTRIBUTE_VARIABLE) @Valid final EditExistingVar editVar,
            final BindingResult bindingResult)
                    throws InvalidIdentifierException
    {
        if (bindingResult.hasErrors())
        {
            // Re-show the edit screen.
            return displayForm(editVar);
        }
        
        // Apply the changes to the persistent variable.
        fAdminService.saveVariable(editVar.applyToVariable());

        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:/admin");
    }
    
}

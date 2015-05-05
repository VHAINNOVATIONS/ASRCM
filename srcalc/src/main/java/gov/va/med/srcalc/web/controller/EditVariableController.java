package gov.va.med.srcalc.web.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.service.*;
import gov.va.med.srcalc.web.view.*;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Web MVC controller for administration of variables.
 */
@Controller
@RequestMapping("/admin/variables/{variableKey}")
public class EditVariableController
{
    private static final String ATTRIBUTE_VARIABLE = "variable";
    
    private final AdminService fAdminService;
    
    @Inject
    public EditVariableController(final AdminService adminService)
    {
        fAdminService = adminService;
    }
    
    @InitBinder
    protected void initBinder(final WebDataBinder binder)
    {
        binder.addValidators(new EditVariableValidator());
    }
    
    /**
     * Creates an {@link EditVariable} instance for the variable to edit.
     * @param variableKey the name of the variable to edit
     * @return the EditVariable instance
     * @throws InvalidIdentifierException
     */
    @ModelAttribute(ATTRIBUTE_VARIABLE)
    public EditVariable createEditVariable(
            @PathVariable final String variableKey)
            throws InvalidIdentifierException
    {
        final EditVariable ev = new EditVariable(
                loadVariable(variableKey), fAdminService.getAllVariableGroups());
        ev.calculateDependentModels(fAdminService.getAllRiskModels());
        return ev;
    }
    
    private AbstractVariable loadVariable(final String variableKey)
            throws InvalidIdentifierException
    {
        return fAdminService.getVariable(variableKey);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView editVariable()
            throws InvalidIdentifierException
    {
        final ModelAndView mav = new ModelAndView(Views.EDIT_BOOLEAN_VARIABLE);
        // Note: "variable" is in the model via createEditVariable() above.
        
        // Add reference data (max lengths, valid values, etc.)
        mav.addObject("DISPLAY_NAME_MAX", Variable.DISPLAY_NAME_MAX);
        
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveVariable(
            @ModelAttribute(ATTRIBUTE_VARIABLE) @Valid final EditVariable editVariable,
            final BindingResult bindingResult)
                    throws InvalidIdentifierException
    {
        if (bindingResult.hasErrors())
        {
            // Re-show the edit screen.
            return editVariable();
        }
        
        // Apply the changes to the persistent variable.
        fAdminService.updateVariable(editVariable.applyToVariable());

        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:/admin/models");
    }
    
}

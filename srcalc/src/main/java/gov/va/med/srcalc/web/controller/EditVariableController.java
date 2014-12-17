package gov.va.med.srcalc.web.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import gov.va.med.srcalc.service.*;
import gov.va.med.srcalc.web.view.Tile;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * Web MVC controller for administration of variables.
 */
@Controller
@RequestMapping("/admin/models/editVariable/{variableKey}")
public class EditVariableController
{
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
     * @return
     * @throws InvalidIdentifierException
     */
    @ModelAttribute("variable")
    public EditVariable createEditVariable(
            @PathVariable final String variableKey)
            throws InvalidIdentifierException
    {
        return EditVariable.fromVariable(fAdminService.getVariable(variableKey));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String editVariable()
            throws InvalidIdentifierException
    {
        // Note: model is populated via addEditVariable() above.
        
        return Tile.EDIT_VARIABLE;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveVariable(
            @ModelAttribute("variable") @Valid final EditVariable editVariable,
            final BindingResult bindingResult)
                    throws InvalidIdentifierException
    {
        if (bindingResult.hasErrors())
        {
            // Re-show the edit screen.
            return editVariable();
        }

        fAdminService.updateVariable(editVariable);
        // Using the POST-redirect-GET pattern.
        return "redirect:/admin/models";
    }
    
}

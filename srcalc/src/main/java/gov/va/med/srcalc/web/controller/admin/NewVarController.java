package gov.va.med.srcalc.web.controller.admin;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Iterables;

import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.web.view.admin.EditVariable;

/**
 * <p>Base Web MVC controller for creating a new variable. Contains common
 * behavior for creating a new variable, only requiring subclasses to define
 * variable type-specific behavior.</p>
 * 
 * <p>Subclasses should only need to implement the abstract methods.</p>
 * 
 * <p>Note: normally we do not abbreviate type names, but we are abbreviating
 * "Variable" here or else some of these controller names would be very long.</p>
 */
public abstract class NewVarController
{
    /**
     * The model attribute key of the {@link EditVariable} object.
     */
    protected static final String ATTRIBUTE_VARIABLE = "variable";
    
    private final AdminService fAdminService;
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     * @throws NullPointerException if adminService is null
     */
    public NewVarController(final AdminService adminService)
    {
        fAdminService = Objects.requireNonNull(adminService);
    }
    
    /**
     * Returns the AdminService provided to the constructor for easy access from
     * subclasses.
     */
    protected final AdminService getAdminService()
    {
        return fAdminService;
    }
    
    /**
     * Returns the URL to save the variable.
     */
    public abstract String getSaveUrl();
    
    /**
     * Creates an {@link EditVariable} instance for creating the new variable.
     */
    @ModelAttribute(ATTRIBUTE_VARIABLE)
    protected abstract EditVariable createEditVariable();
    
    /**
     * Returns the Validators to use for validating the object returned by
     * {@link #createEditVariable()}.
     * @return an Iterable. May be empty, but I don't recommend that.
     */
    protected abstract Iterable<Validator> getValidators();
    
    /**
     * Initializes the given WebDataBinder with the necessary validators.
     */
    @InitBinder
    protected void initBinder(final WebDataBinder binder)
    {
        // addValidators() is varargs, so transform the Iterable into an array.
        binder.addValidators(Iterables.toArray(getValidators(), Validator.class));
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm(
            @ModelAttribute(ATTRIBUTE_VARIABLE) final EditVariable editVariable)
    {
        final ModelAndView mav = new ModelAndView(editVariable.getNewViewName());
        // Provide the URL to save the variable to the view.
        mav.addObject("SAVE_URL", getSaveUrl());
        // Note: "variable" is already in the model via createEditVariable().
        return mav;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveVariable(
            @ModelAttribute(ATTRIBUTE_VARIABLE) @Valid final EditVariable editVariable,
            final BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return displayForm(editVariable);
        }
        
        fAdminService.saveVariable(editVariable.buildNew());
        
        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:/admin");
    }
}

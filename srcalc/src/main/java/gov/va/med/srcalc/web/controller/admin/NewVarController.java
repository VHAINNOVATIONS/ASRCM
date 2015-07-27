package gov.va.med.srcalc.web.controller.admin;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.DuplicateVariableKeyException;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.admin.EditBaseVar;

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
     * The model attribute key of the {@link EditBaseVar} object.
     */
    protected static final String ATTRIBUTE_VARIABLE = "variable";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NewVarController.class);
    
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
     * Creates an {@link EditBaseVar} instance for creating the new variable.
     */
    @ModelAttribute(ATTRIBUTE_VARIABLE)
    protected abstract EditBaseVar createEditBaseVar();
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm(
            @ModelAttribute(ATTRIBUTE_VARIABLE) final EditBaseVar editVar)
    {
        final ModelAndView mav = new ModelAndView(editVar.getNewViewName());
        // Provide the URL to save the variable to the view.
        mav.addObject("SAVE_URL", getSaveUrl());
        // Note: "variable" is already in the model via createEditBaseVar().
        return mav;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveVariable(
            @ModelAttribute(ATTRIBUTE_VARIABLE) final EditBaseVar editVar,
            final BindingResult bindingResult)
    {
        // Spring has already bound the user input to editVar; now validate
        // the input using the appropriate validator.
        //
        // Note that we must do this here instead of configuring the
        // WebDataBinder to use this validator because we need access to the
        // variable-specific editVar instance to call getValidator().
        editVar.getValidator().validate(editVar, bindingResult);

        if (bindingResult.hasErrors())
        {
            LOGGER.debug("EditVar has errors: {}", bindingResult);
            return displayForm(editVar);
        }

        // Note that the validators do not check for uniqueness of the variable
        // key, so we may get here with a duplicate variable key and the below
        // call to saveVariable() may fail. Thus we handle that exception below.
        
        try
        {
            fAdminService.saveVariable(editVar.buildNew());
        }
        // Translate the possible DuplicateVariableKeyException into a
        // validation error.
        catch (final DuplicateVariableKeyException ex)
        {
            bindingResult.rejectValue("key", ValidationCodes.DUPLICATE_VALUE, "duplicate key");
            return displayForm(editVar);
        }
        
        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:" + SrcalcUrls.MODEL_ADMIN_HOME);
    }
}

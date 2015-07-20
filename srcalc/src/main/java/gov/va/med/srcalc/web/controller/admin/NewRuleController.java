package gov.va.med.srcalc.web.controller.admin;

import javax.inject.Inject;

import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.DuplicateRuleNameException;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.web.view.admin.EditRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Web MVC controller for creating a new rule.</p>
 */
@Controller
@RequestMapping(NewRuleController.BASE_URL)
public class NewRuleController extends BaseRuleController
{
    public static final String BASE_URL = "/admin/newRule";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NewRuleController.class);
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     * @throws NullPointerException if adminService is null
     */
    @Inject
    public NewRuleController(final AdminService adminService)
    {
        super(adminService);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm() throws InvalidIdentifierException
    {
        final EditRule editRule = new EditRule();
        return displayForm(editRule);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveRule(
            @ModelAttribute(NewRuleController.ATTRIBUTE_RULE) final EditRule editRule,
            final BindingResult bindingResult) throws InvalidIdentifierException
    {
        // Call the validator for an EditRule here so that we can access the editRule
        // to display 
        editRule.getValidator().validate(editRule, bindingResult);

        if (bindingResult.hasErrors())
        {
            LOGGER.debug("EditRule has errors: {}", bindingResult);
            return displayForm(editRule);
        }

        // Note that the validator does not check for uniqueness of the rule
        // display name, so we may get here with a duplicate rule display name and the below
        // call to saveRule() may fail. Thus we handle that exception below.
        try
        {
            getAdminService().saveRule(editRule.buildNew(getAdminService()));
        }
        // Translate the possible DuplicateRuleNameException into a
        // validation error.
        catch (final DuplicateRuleNameException ex)
        {
            bindingResult.rejectValue("displayName", ValidationCodes.DUPLICATE_VALUE, "duplicate rule name");
            return displayForm(editRule);
        }
        
        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:/admin");
    }
}

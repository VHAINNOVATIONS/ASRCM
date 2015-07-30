package gov.va.med.srcalc.web.controller.admin;

import javax.inject.Inject;

import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.DuplicateRuleNameException;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.admin.EditExistingRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Web MVC controller for editing existing rules.
 */
@Controller
@RequestMapping(EditRuleController.BASE_URL)
public class EditRuleController extends BaseRuleController
{
    public static final String BASE_URL = "/admin/rules/{ruleId}";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EditRuleController.class);
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     * @throws NullPointerException if adminService is null
     */
    @Inject
    public EditRuleController(final AdminService adminService)
    {
        super(adminService);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm(@PathVariable("ruleId") final int ruleId)
            throws InvalidIdentifierException
    {
        final EditExistingRule editRule = new EditExistingRule(
                getAdminService().getRuleById(ruleId));
        return displayForm(editRule);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveRule(
            @ModelAttribute(ATTRIBUTE_RULE) final EditExistingRule editRule,
            final BindingResult bindingResult, @PathVariable("ruleId") final int ruleId)
            throws InvalidIdentifierException
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
            getAdminService().saveRule(
                    editRule.applyToRule(getAdminService(),
                    getAdminService().getRuleById(ruleId)));
        }
        // Translate the possible DuplicateRuleNameException into a
        // validation error.
        catch (final DuplicateRuleNameException ex)
        {
            bindingResult.rejectValue("displayName", ValidationCodes.DUPLICATE_VALUE,
                    "duplicate rule name");
            return displayForm(editRule);
        }
        
        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:" + SrcalcUrls.MODEL_ADMIN_HOME);
    }
}

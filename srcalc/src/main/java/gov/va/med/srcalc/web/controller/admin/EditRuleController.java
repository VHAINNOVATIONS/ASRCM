package gov.va.med.srcalc.web.controller.admin;

import java.util.Objects;

import javax.inject.Inject;

import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.DuplicateRuleNameException;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.EditExistingRule;
import gov.va.med.srcalc.web.view.admin.EditRuleFactory;
import gov.va.med.srcalc.web.view.admin.ValueMatcherBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Web MVC controller for editing existing rules.
 */
@Controller
@RequestMapping("/admin/rules/{ruleId}")
public class EditRuleController
{
    private static final Logger fLogger = LoggerFactory.getLogger(EditRuleController.class);
    
    private final AdminService fAdminService;
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     * @throws NullPointerException if adminService is null
     */
    @Inject
    public EditRuleController(final AdminService adminService)
    {
        fAdminService = Objects.requireNonNull(adminService);
    }
    
    /**
     * Creates an {@link EditExistingRule} instance for the identified
     * rule.
     * @param ruleId the id of the rule to edit
     * @return the EditExistingRule instance
     * @throws InvalidIdentifierException if no such rule exists
     */
    @ModelAttribute(NewRuleController.ATTRIBUTE_RULE)
    private EditExistingRule createEditRuleFromRule(
            @PathVariable("ruleId") final int ruleId) throws InvalidIdentifierException
    {
        fLogger.trace("Creating EditRule from previous Rule with ID: {}.", ruleId);
        final Rule rule = fAdminService.getRuleById(ruleId);
        return EditRuleFactory.getInstance(rule, fAdminService);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayEditRuleForm(
            @ModelAttribute(NewRuleController.ATTRIBUTE_RULE) final EditExistingRule editRule,
            @PathVariable("ruleId") final int ruleId)
                    throws InvalidIdentifierException
    {
        final ModelAndView mav = new ModelAndView(Views.EDIT_RULE);
        mav.addObject("SAVE_URL", ruleId);
        mav.addObject("isNewRule", false);
        NewRuleController.addVariablesToModel(mav, editRule, fAdminService);
        return mav;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView handlePost(
            @ModelAttribute(NewRuleController.ATTRIBUTE_RULE) final EditExistingRule editRule,
            final BindingResult bindingResult,
            @PathVariable("ruleId") final int ruleId,
            @RequestParam(value = "submitButton") final String submitString) throws InvalidIdentifierException
    {
        
        // Test if a remove button was clicked, and remove the proper ValueMatcherBuilder if needed
        if(submitString.startsWith("remove"))
        {
            // Parse the integer that is after the "remove" prefix
            return removeMatcher(editRule, ruleId, Integer.parseInt(submitString.substring(6)));
        }
        else if(submitString.equals("newMatcher"))
        {
            // If we have reached the matcher limit, do not allow any more matchers to be added.
            if(editRule.getMatchers().size() >= Rule.MAX_MATCHERS)
            {
                bindingResult.rejectValue(
                        "matchers",
                        ValidationCodes.TOO_LONG,
                        new Object[] {Rule.MAX_MATCHERS},
                        "too many matchers specified");
                return displayEditRuleForm(editRule, ruleId);
            }
            // Reload the page with the new matcher builder inside of the edit rule
            return addNewMatcher(editRule, ruleId);
        }
        else
        {
            return saveRule(editRule, bindingResult, ruleId, submitString);
        }
    }
    
    private ModelAndView addNewMatcher(final EditExistingRule editRule,
            final int ruleId) throws InvalidIdentifierException
    {
        editRule.getMatchers().add(
                new ValueMatcherBuilder(editRule.getNewVariableKey()));
        return displayEditRuleForm(editRule, ruleId);
    }
    
    private ModelAndView removeMatcher(final EditExistingRule editRule, final int ruleId,
            final int removeIndex) throws InvalidIdentifierException
    {
        editRule.getMatchers().remove(removeIndex);
        return displayEditRuleForm(editRule, ruleId);
    }
    
    private ModelAndView saveRule(final EditExistingRule editRule,
            final BindingResult bindingResult,
            final int ruleId,
            final String submitString) throws InvalidIdentifierException
    {
        // Call the validator for an EditRule here so that we can access the editRule
        // to display 
        editRule.getValidator().validate(editRule, bindingResult);

        if (bindingResult.hasErrors())
        {
            fLogger.debug("EditRule has errors: {}", bindingResult);
            return displayEditRuleForm(editRule, ruleId);
        }

        // Note that the validators do not check for uniqueness of the rule
        // display name, so we may get here with a duplicate rule display name and the below
        // call to saveRule() may fail. Thus we handle that exception below.
        
        try
        {
            fAdminService.saveRule(editRule.applyToRule());
        }
        // Translate the possible DuplicateRuleNameException into a
        // validation error.
        catch (final DuplicateRuleNameException ex)
        {
            bindingResult.rejectValue("displayName", ValidationCodes.DUPLICATE_VALUE, "duplicate rule name");
            return displayEditRuleForm(editRule, ruleId);
        }
        
        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:/admin");
    }
}

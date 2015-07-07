package gov.va.med.srcalc.web.controller.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.DuplicateRuleNameException;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.web.view.VariableSummary;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.EditRule;
import gov.va.med.srcalc.web.view.admin.ValueMatcherBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Web MVC controller for creating a new rule.</p>
 */
@Controller
public class NewRuleController
{
    public static final String BASE_URL = "/admin/newRule";
    
    private static final Logger fLogger = LoggerFactory.getLogger(NewRuleController.class);
    /**
     * The model attribute key of the {@link Rule} object.
     */
    protected static final String ATTRIBUTE_RULE = "rule";
    
    private final AdminService fAdminService;
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     * @throws NullPointerException if adminService is null
     */
    @Inject
    public NewRuleController(final AdminService adminService)
    {
        fAdminService = Objects.requireNonNull(adminService);
    }
    
    @ModelAttribute(ATTRIBUTE_RULE)
    private EditRule createEditRule()
    {
        fLogger.trace("Creating EditRule.");
        return new EditRule(fAdminService);
    }
    
    @RequestMapping(value = BASE_URL, method = RequestMethod.GET)
    public ModelAndView displayForm(
            @ModelAttribute(ATTRIBUTE_RULE) final EditRule editRule) throws InvalidIdentifierException
    {
        final ModelAndView mav = new ModelAndView(Views.EDIT_RULE);
        // Provide the URL to save the rule to the view.
        mav.addObject("SAVE_URL", BASE_URL);
        // Note: "rule" is already in the model via createEditRule().
        mav.addObject("isNewRule", true);
        addVariablesToModel(mav, editRule, fAdminService);
        return mav;
    }
    
    @RequestMapping(value = BASE_URL, method = RequestMethod.POST)
    public ModelAndView handlePost(
            @ModelAttribute(ATTRIBUTE_RULE) final EditRule editRule,
            final BindingResult bindingResult,
            @RequestParam(value = "submitButton") final String submitString) throws InvalidIdentifierException
    {
        // Test if a remove button was clicked, and remove the proper ValueMatcherBuilder if needed
        if(submitString.startsWith("remove"))
        {
            // Parse the integer that is after the "remove" prefix
            return removeMatcher(editRule, Integer.parseInt(submitString.substring(6)));
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
                return displayForm(editRule);
            }
            // Reload the page with the new matcher builder inside of the edit rule
            return addNewMatcher(editRule);
        }
        else
        {
            return saveRule(editRule, bindingResult, submitString);
        }
    }
    
    private ModelAndView addNewMatcher(final EditRule editRule) throws InvalidIdentifierException
    {
        editRule.getMatchers().add(
                new ValueMatcherBuilder(editRule.getNewVariableKey()));
        
        return displayForm(editRule);
    }
    
    private ModelAndView removeMatcher(final EditRule editRule,
            final int removeIndex) throws InvalidIdentifierException
    {
        editRule.getMatchers().remove(removeIndex);
        return displayForm(editRule);
    }
    
    private ModelAndView saveRule(final EditRule editRule, final BindingResult bindingResult,
            final String submitString) throws InvalidIdentifierException
    {
        // Call the validator for an EditRule here so that we can access the editRule
        // to display 
        editRule.getValidator().validate(editRule, bindingResult);

        if (bindingResult.hasErrors())
        {
            fLogger.debug("EditRule has errors: {}", bindingResult);
            return displayForm(editRule);
        }

        // Note that the validators do not check for uniqueness of the rule
        // display name, so we may get here with a duplicate rule display name and the below
        // call to saveRule() may fail. Thus we handle that exception below.
        
        try
        {
            fAdminService.saveRule(editRule.buildNew());
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
    
    protected static void addVariablesToModel(final ModelAndView mav,
            final EditRule editRule,
            final AdminService adminService) throws InvalidIdentifierException
    {
        // A map of the variable's key to its respective summary
        final Map<String, VariableSummary> variableSummaries = new HashMap<String, VariableSummary>();
        for(final ValueMatcherBuilder matcher: editRule.getMatchers())
        {
            final Variable var = adminService.getVariable(matcher.getVariableKey());
            variableSummaries.put(matcher.getVariableKey(), VariableSummary.fromVariable(var));
        }
        mav.addObject("variableSummaries", variableSummaries);
        final SortedSet<String> allVariableKeys = new TreeSet<String>();
        for(final Variable var: adminService.getAllVariables())
        {
            allVariableKeys.add(var.getKey());
        }
        mav.addObject("allVariableKeys", allVariableKeys);
    }
}

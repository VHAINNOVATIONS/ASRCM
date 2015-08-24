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
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.web.view.VariableSummary;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.EditRule;
import gov.va.med.srcalc.web.view.admin.ValueMatcherBuilder;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
* <p>Base Web MVC controller for creating or editing rules. Contains common
* behavior for creating and editing rules.</p>
*/ 
@Controller
public abstract class BaseRuleController
{
    /**
     * The model attribute key of the {@link Rule} object.
     */
    protected static final String ATTRIBUTE_RULE = "rule";
    
    /**
     * The model attribute key of a map from variable key to {@link VariableSummary}.
     * Will contain an entry for every variable used by the rule.
     */
    protected static final String ATTRIBUTE_VARIABLE_SUMMARIES = "variableSummaries";
    
    /**
     * The model attribute key of a list of all existing variable keys.
     */
    protected static final String ATTRIBUTE_ALL_VARIABLE_KEYS = "allVariableKeys";
    
    private final AdminService fAdminService;
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     * @throws NullPointerException if adminService is null
     */
    @Inject
    public BaseRuleController(final AdminService adminService)
    {
        fAdminService = Objects.requireNonNull(adminService);
    }
    
    /**
     * Presents the edit rule form, backed by the given EditRule object.
     * @param editRule the form backing object
     * @throws InvalidIdentifierException if any variable key referenced by the EditRule
     * does not exist
     */
    protected ModelAndView displayForm(final EditRule editRule) throws InvalidIdentifierException
    {
        final ModelAndView mav = new ModelAndView(Views.EDIT_RULE);
        mav.addObject(ATTRIBUTE_RULE, editRule);
        addVariablesToModel(mav, editRule, getAdminService());
        return mav;
    }

    /**
     * Removes the specified ValueMatcher from the form backing object and re-presents the
     * edit rule page.
     * @param editRule the form backing object
     * @param removeIndex the index of the ValueMatcher to remove
     * @throws InvalidIdentifierException if any variable key referenced by the EditRule
     * does not exist
     */
    @RequestMapping(method = RequestMethod.POST, params = "removeButton")
    public ModelAndView requestRemoveMatcher(
            @ModelAttribute(NewRuleController.ATTRIBUTE_RULE) final EditRule editRule,
            @RequestParam(value = "removeButton") final int removeIndex) throws InvalidIdentifierException
    {
        // Parse the integer that is after the "remove" prefix
        editRule.getMatchers().remove(removeIndex);
        return displayForm(editRule);
    }
    
    /**
     * Adds a new ValueMatcher to the form backing object.
     * @param editRule the form backing object
     * @param bindingResult the BindingResult for the EditRule
     * @throws InvalidIdentifierException if any variable key in the EditRule does not
     * exist
     */
    @RequestMapping(method = RequestMethod.POST, params = "newMatcherButton")
    public ModelAndView requestNewMatcher(
            @ModelAttribute(NewRuleController.ATTRIBUTE_RULE) final EditRule editRule,
            final BindingResult bindingResult)
            throws InvalidIdentifierException
    {
        // If we have reached the matcher limit, do not allow any more matchers to be added.
        if (editRule.getMatchers().size() >= EditRule.MAX_MATCHERS)
        {
            bindingResult.rejectValue("matchers", ValidationCodes.TOO_LONG,
                    new Object[] { EditRule.MAX_MATCHERS }, "too many matchers specified");
            return displayForm(editRule);
        }
        // Reload the page with the new matcher builder inside of the edit rule
        editRule.getMatchers().add(new ValueMatcherBuilder(editRule.getNewVariableKey()));
        return displayForm(editRule);
    }
    
    /**
     * Adds {@link #ATTRIBUTE_VARIABLE_SUMMARIES} and {@link #ATTRIBUTE_ALL_VARIABLE_KEYS}
     * to the given ModelAndView.
     * @param mav the ModelAndView to modify
     * @param editRule to determine which {@link VariableSummary}s to load
     * @param adminService to retrieve variables
     * @throws InvalidIdentifierException if any variable key referenced by the EditRule
     * does not exist
     */
    private static void addVariablesToModel(
            final ModelAndView mav,
            final EditRule editRule,
            final AdminService adminService)
            throws InvalidIdentifierException
    {
        final Map<String, VariableSummary> variableSummaries = new HashMap<String, VariableSummary>();
        for(final ValueMatcherBuilder matcher: editRule.getMatchers())
        {
            final Variable var = adminService.getVariable(matcher.getVariableKey());
            variableSummaries.put(matcher.getVariableKey(), VariableSummary.fromVariable(var));
        }
        mav.addObject(ATTRIBUTE_VARIABLE_SUMMARIES, variableSummaries);

        final SortedSet<String> allVariableKeys = new TreeSet<String>();
        for(final Variable var: adminService.getAllVariables())
        {
            allVariableKeys.add(var.getKey());
        }
        mav.addObject(ATTRIBUTE_ALL_VARIABLE_KEYS, allVariableKeys);
    }
    
    /**
     * Returns the {@link AdminService} provided to the constructor.
     */
    protected AdminService getAdminService()
    {
        return fAdminService;
    }
}

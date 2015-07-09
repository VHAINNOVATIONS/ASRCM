package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.domain.model.ValueMatcher;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;

/**
 * <p>A form backing object for creating a new or editing an existing
 * Rule.</p>
 */
public class EditRule
{
    public static final int MAX_MATCHERS = 10;
    
    private AdminService fAdminService;
    private final List<ValueMatcherBuilder> fMatchers;
    private String fSummandExpression;
    private boolean fBypassEnabled;
    private String fDisplayName;
    private String fNewVariableKey;
    
    /**
     * Because we are not using the ModelAttribute annotation to create default information
     * anymore, Spring needs to have a default constructor to initialize the bean.
     */
    EditRule()
    {
        fAdminService = null;
        fMatchers = new ArrayList<ValueMatcherBuilder>();
        fSummandExpression = "";
        fBypassEnabled = false;
        fDisplayName = "";
        fNewVariableKey = "";
    }
    
    /**
     * Constructs an instance with default values and an empty list of 
     * ValueMatcherBuilder objects.
     * @param adminService to provide reference data (e.g., getting a variable
     * that is tied to a ValueMatcher) to the user
     */
    public EditRule(final AdminService adminService) 
    {
        fAdminService = adminService;
        fMatchers = new ArrayList<ValueMatcherBuilder>();
        fSummandExpression = "";
        fBypassEnabled = false;
        fDisplayName = "";
        fNewVariableKey = "";
    }
    
    /**
     * Constructs an instance that is filled with the same information that the rule
     * 
     * @param adminService to provide reference data (e.g., getting a variable
     * that is tied to a ValueMatcher) to the user
     * @param rule the rule to copy into this EditRule
     */
    public EditRule(final AdminService adminService, final Rule rule)
    {
        // Copy the value matchers
        fMatchers = new ArrayList<ValueMatcherBuilder>(rule.getMatchers().size());
        for(final ValueMatcher matcher: rule.getMatchers())
        {
            fMatchers.add(ValueMatcherBuilder.fromPrototype(matcher));
        }
        fSummandExpression = rule.getSummandExpression();
        fBypassEnabled = rule.isBypassEnabled();
        fDisplayName = rule.getDisplayName();
        fNewVariableKey = null;
    }
    
    public void setAdminService(final AdminService adminService)
    {
        fAdminService = adminService;
    }
    
    /**
     * Returns the list of ValueMatcherBuilders for this EditRule.
     */
    public List<ValueMatcherBuilder> getMatchers()
    {
        return fMatchers;
    }

    /**
     * Returns the summand expression for this EditRule in String form.
     */
    public String getSummandExpression()
    {
        return fSummandExpression;
    }

    public void setSummandExpression(final String summandExpression)
    {
        this.fSummandExpression = summandExpression;
    }

    /**
     * Returns true if this rule is required and false if it is not required.
     */
    public boolean isBypassEnabled()
    {
        return fBypassEnabled;
    }

    public void setBypassEnabled(final boolean bypassEnabled)
    {
        this.fBypassEnabled = bypassEnabled;
    }

    /**
     * Returns this EditRule's display name.
     */
    public String getDisplayName()
    {
        return fDisplayName;
    }

    public void setDisplayName(final String displayName)
    {
        this.fDisplayName = displayName;
    }
    
    /**
     * Returns the variable key used to identify a new variable being added to
     * this EditRule
     */
    public String getNewVariableKey()
    {
        return fNewVariableKey;
    }
    
    public void setNewVariableKey(final String newVariableKey)
    {
        fNewVariableKey = newVariableKey;
    }
    
    /**
     * Returns true if this object is editing an existing rule.
     */
    public boolean isEditingRule()
    {
        return false;
    }

    /**
     * Returns a validator for an EditRule
     */
    public EditRuleValidator getValidator()
    {
        return new EditRuleValidator();
    }
    
    /**
     * Returns all variables that this EditRule requires.
     */
    public Set<String> getRequiredVariableKeys()
    {
        final SortedSet<String> variableKeys = new TreeSet<String>();
        for (final ValueMatcherBuilder vm : fMatchers)
        {
            variableKeys.add(vm.getVariableKey());
        }
        return ImmutableSet.copyOf(variableKeys);
    }
    
    /**
     * Build a new {@link Rule} object from this EditRule.
     * @return a Rule with the same information as this EditRule
     * @throws InvalidIdentifierException if one of the variable keys present in the builder
     * does not exist in the database.
     */
    public Rule buildNew() throws InvalidIdentifierException
    {
        final List<ValueMatcher> matchers = new ArrayList<ValueMatcher>(fMatchers.size());
        for(final ValueMatcherBuilder builder: fMatchers)
        {
            matchers.add(builder.buildNew(fAdminService));
        }
        // Negate fBypassEnabled because our internal Rule stores a required field, but
        // the user sees references to bypassing the rule if it is missing values.
        final Rule rule = new Rule(matchers, fSummandExpression, !fBypassEnabled, fDisplayName);
        return rule;
    }
}

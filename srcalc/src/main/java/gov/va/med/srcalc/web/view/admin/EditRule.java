package gov.va.med.srcalc.web.view.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.domain.model.ValueMatcher;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;

public class EditRule
{
    private AdminService fAdminService;
    private ArrayList<ValueMatcherBuilder> fMatchers;
    private String fSummandExpression;
    private boolean fRequired;
    private String fDisplayName;
    private String fNewVariableKey;
    
    public EditRule(final AdminService adminService) 
    {
        fAdminService = adminService;
        fMatchers = new ArrayList<ValueMatcherBuilder>();
        fSummandExpression = "";
        fRequired = false;
        fDisplayName = "";
        fNewVariableKey = null;
    }
    
    public EditRule(final AdminService adminService, final Rule rule)
    {
        // Copy the value matchers
        fMatchers = new ArrayList<ValueMatcherBuilder>(rule.getMatchers().size());
        for(final ValueMatcher matcher: rule.getMatchers())
        {
            fMatchers.add(ValueMatcherBuilder.fromPrototype(matcher));
        }
        fSummandExpression = rule.getSummandExpression();
        fRequired = rule.isRequired();
        fDisplayName = rule.getDisplayName();
        fNewVariableKey = null;
    }
    
    /**
     * Returns the list of ValueMatcherBuilders for this EditRule.
     */
    public List<ValueMatcherBuilder> getMatchers()
    {
        return fMatchers;
    }

    public void setMatchers(final ArrayList<ValueMatcherBuilder> matchers)
    {
        this.fMatchers = matchers;
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
    public boolean getRequired()
    {
        return fRequired;
    }

    public void setRequired(final boolean required)
    {
        this.fRequired = required;
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
     * Returns a validator for an EditRule
     */
    public EditRuleValidator getValidator()
    {
        return new EditRuleValidator();
    }
    
    /**
     * Returns all variables that this EditRule requires.
     * @throws InvalidIdentifierException
     */
    public Set<Variable> getRequiredVariables() throws InvalidIdentifierException
    {
        final HashSet<Variable> variables = new HashSet<>();
        for (final ValueMatcherBuilder vm : fMatchers)
        {
            variables.add(fAdminService.getVariable(vm.getVariableKey()));
        }
        return ImmutableSet.copyOf(variables);
    }
    
    /**
     * Build a new {@link Rule} object from this EditRule.
     * @return a Rule with the same information as this EditRule
     * @throws InvalidIdentifierException
     */
    public Rule buildNew() throws InvalidIdentifierException
    {
        final List<ValueMatcher> matchers = new ArrayList<ValueMatcher>(fMatchers.size());
        for(final ValueMatcherBuilder builder: fMatchers)
        {
            matchers.add(new ValueMatcher(
                    fAdminService.getVariable(builder.getVariableKey()),
                    builder.getBooleanExpression(),
                    builder.isEnabled()));
        }
        final Rule rule = new Rule(matchers, fSummandExpression, fRequired, fDisplayName);
        return rule;
    }
}

package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;

/**
 * <p>An interface for editing an existing {@link Rule}.</p>
 * 
 * <p>The high-level workflow is:</p>
 * 
 * <ol>
 * <li>Construct an instance based on an existing rule.</li>
 * <li>Present the user with the current values and allow her to update the
 * properties as desired.</li>
 * <li>Use the {@link EditRuleValidator} validator to validate the user's edits.</li>
 * <li>Call {@link #applyToRule()} to update the target rule with the
 * new properties.</li>
 * </ol>
 */
public class EditExistingRule extends EditRule
{
    private Rule fTarget;
    private AdminService fAdminService;

    /**
     * Because we are not using the ModelAttribute annotation to create default information
     * anymore, Spring needs to have a default constructor to initialize the bean.
     */
    public EditExistingRule()
    {
        super();
        fTarget = null;
        fAdminService = null;
    }
    
    /**
     * Constructs an instance to edit the given rule.
     * @param target the rule to edit
     * @param adminService to provide reference data (e.g.  to the user
     */
    public EditExistingRule(final AdminService adminService, final Rule target)
    {
        super(adminService, target);
        fAdminService = adminService;
        fTarget = target;
    }
    
    /**
     * Applies the specified changes to the target rule.
     * @return the target rule for convenience
     * @throws IllegalStateException if any changes are invalid
     */
    public Rule applyToRule() throws InvalidIdentifierException
    {
        // Set the necessary fields on fTarget
        fTarget.setDisplayName(this.getDisplayName());
        fTarget.getMatchers().clear();
        for(final ValueMatcherBuilder builder: getMatchers())
        {
            fTarget.getMatchers().add(builder.buildNew(fAdminService));
        }
        fTarget.setBypassEnabled(this.isBypassEnabled());
        fTarget.setSummandExpression(getSummandExpression());
        return fTarget;
    }
    
    public void setAdminService(final AdminService adminService)
    {
        fAdminService = adminService;
    }
    
    /**
     * Returns the rule that will have changes made to it.
     */
    public Rule getTargetRule()
    {
        return fTarget;
    }
    
    public void setTarget(final Rule rule)
    {
        fTarget = rule;
    }
    
    @Override
    public boolean isEditingRule()
    {
        return true;
    }
    
    @Override
    public String toString()
    {
        return String.format("Name: %s Summand Expression: %s Matchers: %s Target Rule: %s", 
                this.getDisplayName(), this.getSummandExpression(), this.getMatchers(),
                this.getTargetRule());
    }
}

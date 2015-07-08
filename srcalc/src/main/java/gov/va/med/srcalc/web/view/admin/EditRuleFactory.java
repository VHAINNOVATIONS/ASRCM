package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;

/**
 * Constructs an appropriate instance of {@link EditExistingRule} to edit an
 * existing rule.
 */
public class EditRuleFactory
{
    /**
     * Returns an {@link EditExistingRule} instance for editing the given rule.
     * @param ruleId the unique identifier of the rule to edit
     * @param adminService for EditExistingRule implementations to retrieve any
     * necessary reference data from the required Variables
     * @throws InvalidIdentifierException if there is no Rule with the specified ID.
     */
    public static EditExistingRule getInstance(
            final int ruleId,
            final AdminService adminService) throws InvalidIdentifierException
    {
        return new EditExistingRule(adminService, adminService.getRuleById(ruleId));
    }
}

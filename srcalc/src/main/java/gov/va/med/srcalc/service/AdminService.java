package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.variable.Variable;

import java.util.List;

/**
 * Service Layer facade for administrative functionalty.
 * @see gov.va.med.srcalc.service
 */
public interface AdminService
{
    /**
     * Returns all Variables for editing purposes.
     */
    public List<Variable> getAllVariables();
    
    /**
     * Returns the Variable with the given display name.
     * @throws InvalidIdentifierException if no such Variable exists
     */
    public Variable getVariable(final String displayName)
        throws InvalidIdentifierException;
    
    /**
     * Updates a Variable with the given properties.
     * @param displayName the current display name of the Variable to edit
     * @param properties contains properties to set
     * @throws InvalidIdentifierException if no such Variable exists
     */
    public void updateVariable(
            final String displayName, final EditVariable properties)
        throws InvalidIdentifierException;
}

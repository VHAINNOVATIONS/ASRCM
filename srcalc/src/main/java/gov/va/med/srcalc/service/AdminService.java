package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.AbstractVariable;

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
    public List<AbstractVariable> getAllVariables();
    
    /**
     * Returns the Variable with the given display name.
     * @throws InvalidIdentifierException if no such Variable exists
     */
    public AbstractVariable getVariable(final String key)
        throws InvalidIdentifierException;
    
    /**
     * Updates a Variable with the given command object. See the package javadocs
     * for why we use a command object.
     * @param command a command object containing the edits to make
     * @throws InvalidIdentifierException if no such Variable exists
     */
    public void updateVariable(final EditVariable command)
        throws InvalidIdentifierException;
}

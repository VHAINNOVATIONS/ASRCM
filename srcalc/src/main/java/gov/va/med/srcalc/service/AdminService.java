package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.*;

import java.util.*;

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
     * Returns all VariableGroups for editing purposes.
     * @return a set sorted by the VariableGroups' natural order
     */
    public SortedSet<VariableGroup> getAllVariableGroups();
    
    /**
     * Returns the Variable with the given display name for editing. Note that
     * the returned object must be given back to {@link
     * #updateVariable(AbstractVariable)} to persist any changes.
     * @throws InvalidIdentifierException if no such Variable exists
     */
    public AbstractVariable getVariable(final String key)
        throws InvalidIdentifierException;
    
    /**
     * Updates the given variable in the persistent store.
     * @param variable the variable to update. The object must have been
     * previously loaded using {@link #getVariable(String)}.
     */
    public void updateVariable(final AbstractVariable variable);
    
    /**
     * Returns all {@link RiskModel}s in the database.
     * @return a collection in arbitrary order
     */
    public Collection<RiskModel> getAllRiskModels();
}

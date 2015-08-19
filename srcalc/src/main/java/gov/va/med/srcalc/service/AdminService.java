package gov.va.med.srcalc.service;

import java.util.Set;

import org.springframework.dao.DataAccessException;

import gov.va.med.srcalc.domain.model.*;

/**
 * Service Layer facade for administering risk model definitions (including specialties).
 * @see gov.va.med.srcalc.service
 */
public interface AdminService extends ModelInspectionService
{
    /**
     * Returns the Variable with the given display name for editing. Note that
     * the returned object must be given back to {@link
     * #saveVariable(AbstractVariable)} to persist any changes.
     */
    @Override
    public AbstractVariable getVariable(final String key)
        throws InvalidIdentifierException;
    
    /**
     * Saves the given variable to the persistent store. The given variable may
     * be brand-new or one previously loaded by {@link #getVariable(String)}.
     * @param variable the variable to save
     * @throws DuplicateVariableKeyException if the provided variable key is
     * non-unique
     * @throws DataAccessException if any other error occurs when trying to save
     * the variable to the persistent store
     */
    public void saveVariable(final AbstractVariable variable)
            // Declare these exceptions even though they are unchecked because calling
            // code should handle them (unlike most unchecked exceptions).
            throws DuplicateVariableKeyException, DataAccessException;
    
    /**
     * Returns the {@link Rule} with the given display name for editing. Note that
     * the returned object must be given back to {@link #saveRule(Rule)}
     * to persist any changes.
     */
    @Override
    public Rule getRule(final String displayName) throws InvalidIdentifierException;
    
    /**
     * Returns the {@link Rule} with the given unique id for editing. Note that
     * the returned object must be given back to {@link #saveRule(Rule)}
     * to persist any changes.
     * @throws InvalidIdentifierException if no such Rule exists
     */
    public Rule getRuleById(final int id) throws InvalidIdentifierException;
    
    
    /**
     * Saves the given rule to the persistent store. The given rule may be
     * brand-new or one previously loaded by {@link #getRule(String)}.
     * @param rule the rule to save
     * @throws DuplicateRuleNameException if the provided rule key is non-unique
     */
    public void saveRule(final Rule rule)
            // Declare this exception even though it is unchecked because calling code
            // should handle it (unlike most unchecked exceptions).
            throws DuplicateRuleNameException;
    
    /**
     * Completely replaces all Procedures in the persistent store with the given set.
     * @param newProcedures the new procedure set
     */
    public void replaceAllProcedures(final Set<Procedure> newProcedures);

    /**
     * Saves the given {@link RiskModel} to the persistent store.
     * @param model the model to save
     */
    public void saveRiskModel( final RiskModel model );
}

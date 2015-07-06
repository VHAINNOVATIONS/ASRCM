package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.*;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

/**
 * <p>Retrieves various Risk Model objects for inspection. This service is useful for
 * both administration and running calculations (e.g., for selecting a Procedure).</p>
 * 
 * <p>This interface does not provide a means to persist any changes, and returned objects
 * are not guaranteed to support mutation.</p>
 */
public interface ModelInspectionService
{
    /**
     * Returns all Variables in the database.
     * @return an ImmutableList, in display name order
     */
    public ImmutableList<AbstractVariable> getAllVariables();

    /**
     * Returns the Variable with the given key.
     * @param key the unique key of the variable to retrieve
     * @return the Variable instance. Never null.
     * @throws InvalidIdentifierException if no such Variable exists
     */
    public AbstractVariable getVariable(final String key)
        throws InvalidIdentifierException;
    
    /**
     * Returns all VariableGroups in the database..
     * @return an ImmutableCollection, in arbitrary order
     */
    public ImmutableCollection<VariableGroup> getAllVariableGroups();
    
    /**
     * Returns all {@link RiskModel}s in the database.
     * @return an ImmutableCollection, in arbitrary order
     */
    public ImmutableCollection<RiskModel> getAllRiskModels(); 
    
    /**
     * Returns all Rules defined in the database.
     * @return a list, in display name order
     */
    public ImmutableCollection<Rule> getAllRules();

    /**
     * Returns all available Procedures, ordered by CPT code for convenience.
     * @return an immutable list
     */
    public ImmutableList<Procedure> getAllProcedures();
    
    /**
     * Returns the {@link RiskModel} with the given ID.
     * @return RiskModelS
     */
    public RiskModel getRiskModelForId(int modelId);
}

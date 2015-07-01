package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.domain.model.VariableGroup;

import com.google.common.collect.ImmutableCollection;

/**
 * <p>Retrieves various Risk Model objects for inspection.</p>
 * 
 * <p>All returned objects are intended for inspection only. This interface
 * does not provide a means to persist any changes.</p>
 */
public interface ModelInspectionService
{
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
     * Returns the {@link RiskModel} with the given ID.
     * @return RiskModelS
     */
    public RiskModel getRiskModelForId(int modelId);
}

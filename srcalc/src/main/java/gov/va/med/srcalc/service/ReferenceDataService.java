package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.Procedure;

import com.google.common.collect.ImmutableList;

/**
 * A service that exposes reference data (e.g., Procedure lists) necessary for completing
 * a calculation.
 */
public interface ReferenceDataService
{
    /**
     * Returns all available Procedures, ordered by CPT code for convenience.
     * @return an immutable list
     */
    public ImmutableList<Procedure> getAllProcedures();
}

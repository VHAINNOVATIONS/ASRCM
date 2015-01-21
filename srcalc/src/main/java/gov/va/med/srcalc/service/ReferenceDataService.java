package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.Procedure;

import java.util.List;

/**
 * A Service that simply exposes reference data (e.g., Procedure lists) to
 * callers.
 */
public interface ReferenceDataService
{
    public List<Procedure> getActiveProcedures();
}

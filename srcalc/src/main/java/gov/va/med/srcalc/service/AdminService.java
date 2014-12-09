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
}

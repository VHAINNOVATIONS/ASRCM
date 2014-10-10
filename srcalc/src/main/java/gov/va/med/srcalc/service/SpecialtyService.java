package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.Specialty;

import java.util.List;

/**
 * Interface for SpecialtyService. All srcalc services implement interfaces to
 * allow proxies (e.g., for transaction management). Clients should therefore
 * depend on this interface, not on {@link DefaultSpecialtyService}.
 */
public interface SpecialtyService
{
    public List<Specialty> getAllSpecialties();
}

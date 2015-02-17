package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.domain.Patient;

/**
 * Data Access Object for VistA Patients.
 */
public interface VistaPatientDao
{
    public Patient getPatient(final int dfn);
    
}

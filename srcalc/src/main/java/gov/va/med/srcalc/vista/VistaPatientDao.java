package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.domain.Patient;

/**
 * Data Access Object for VistA Patients.
 */
public interface VistaPatientDao
{
    /**
     * Loads a Patient from VistA given his/her DFN.
     * @throws DataAccessException if communication with VistA failed
     */
    public Patient getPatient(final int dfn);
    
}

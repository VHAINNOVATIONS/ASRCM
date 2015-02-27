package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.domain.Patient;

/**
 * A mock {@link VistaPatientDao} just for tests.
 */
public class MockVistaPatientDao implements VistaPatientDao
{
    @Override
    public Patient getPatient(int dfn)
    {
        return new Patient(dfn, "PATIENT,TEST" + dfn, "M", 40);
    }
    
}

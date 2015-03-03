package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.SampleObjects;

/**
 * A mock {@link VistaPatientDao} just for tests.
 */
public class MockVistaPatientDao implements VistaPatientDao
{
    @Override
    public Patient getPatient(int dfn)
    {
    	return SampleObjects.dummyPatient();
    }
    
}

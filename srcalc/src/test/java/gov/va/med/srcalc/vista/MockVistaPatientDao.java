package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.SampleObjects;

/**
 * A mock {@link VistaPatientDao} just for tests.
 */
public class MockVistaPatientDao implements VistaPatientDao
{
    @Override
    public Patient getPatient(final int dfn)
    {
    	return SampleObjects.dummyPatient(dfn);
    }

    /**
     * Assumes the dummy result is returning a valid save.
     */
	@Override
	public SaveNoteCode saveRiskCalculationNote(final Patient patient,
			final String electronicSignature, final String noteBody)
	{
            return SaveNoteCode.SUCCESS;
	}
    
}

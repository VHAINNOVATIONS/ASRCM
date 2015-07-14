package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;

/**
 * A mock {@link VistaPatientDao} just for tests.
 */
public class MockVistaPatientDao implements VistaPatientDao
{
    @Override
    public Patient getPatient(final int dfn)
    {
        return SampleCalculations.dummyPatient(dfn);
    }

    /**
     * Assumes the dummy result is returning a valid save.
     */
    @Override
    public SaveNoteCode saveRiskCalculationNote(final int patientDfn,
            final String electronicSignature, final String noteBody)
    {
        return SaveNoteCode.SUCCESS;
    }
    
}

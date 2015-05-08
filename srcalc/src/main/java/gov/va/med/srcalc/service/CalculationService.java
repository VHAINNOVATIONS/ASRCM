package gov.va.med.srcalc.service;

import java.util.List;

import gov.va.med.srcalc.domain.calculation.Calculation;
import gov.va.med.srcalc.domain.calculation.Value;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.util.MissingValuesException;
import gov.va.med.srcalc.vista.VistaPatientDao;

public interface CalculationService
{
    /**
     * Returns a list of all specialties valid for new calulations, sorted by
     * name.
     */
    public List<Specialty> getValidSpecialties();
    
    /**
     * Initiates a new calculation for a particular patient.
     * @param patientId the patient identifier (DFN)
     * @return the newly-started calculation object
     */
    public Calculation startNewCalculation(int patientId);

    /**
     * Sets the specialty for the Calculation, moving the Calculation into the
     * next step of the workflow.
     * @param calculation
     * @param specialtyName
     * @throws InvalidIdentifierException if the specialty name is invalid
     */
    public void setSpecialty(Calculation calculation, String specialtyName)
        throws InvalidIdentifierException;
    
    public void runCalculation(Calculation calculation, List<Value> variableValues)
            throws MissingValuesException;

    /**
     * Saves the finished calculation to VistA, given the calculation, electronic signature, 
     * and the note body
     * @param calculation
     * @return one of the {@link VistaPatientDao.SaveNoteCode} return codes
     */
    public VistaPatientDao.SaveNoteCode saveRiskCalculationNote(
            Calculation calculation, String electronicSignature);
}

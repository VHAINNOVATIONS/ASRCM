package gov.va.med.srcalc.service;

import java.util.Collection;
import java.util.List;

import gov.va.med.srcalc.domain.calculation.*;
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
    
    /**
     * Runs the calculation with the given input values.
     * @param calculation the in-progress calculation
     * @param variableValues the input values
     * @return the results as a CalculationResult object
     * @throws MissingValuesException if any required values are missing
     */
    public CalculationResult runCalculation(Calculation calculation, Collection<Value> variableValues)
            throws MissingValuesException;

    /**
     * Saves the given calculation result to VistA.
     * @param result the result to save
     * @param electronicSignature the electronic signature code of the signing
     * user
     * @return one of the {@link VistaPatientDao.SaveNoteCode} return codes
     */
    public VistaPatientDao.SaveNoteCode signRiskCalculation(
            CalculationResult result, String electronicSignature);
}

package gov.va.med.srcalc.service;

import java.util.List;

import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.variable.Value;
import gov.va.med.srcalc.domain.workflow.*;
import gov.va.med.srcalc.util.MissingValueListException;

public interface CalculationService
{
    /**
     * Initiates a new calculation for a particular patient.
     * @param patientId may not be an int for long...
     */
    public NewCalculation startNewCalculation(int patientId);

    /**
     * Sets the specialty for the Calculation, moving the Calculation into the
     * next step of the workflow.
     * @param calculation
     * @param specialtyName
     * @throws InvalidIdentifierException if the specialty name is invalid
     */
    public SelectedCalculation setSpecialty(Calculation calculation, String specialtyName)
        throws InvalidIdentifierException;
    
    public CalculationWorkflow runCalculation(Calculation calculation, List<Value> variableValues) throws MissingValueListException;
}

package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.workflow.NewCalculation;

public interface CalculationService
{
    /**
     * Initiates a new calculation for a particular patient.
     * @param patientId may not be an int for long...
     */
    public NewCalculation startNewCalculation(int patientId);
}

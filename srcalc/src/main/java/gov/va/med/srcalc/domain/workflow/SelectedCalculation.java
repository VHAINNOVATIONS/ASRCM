package gov.va.med.srcalc.domain.workflow;

import gov.va.med.srcalc.domain.Calculation;

/**
 * A Calculation for which the risk model has been selected.
 */
public class SelectedCalculation extends CalculationWorkflow
{
    public SelectedCalculation(Calculation calculation)
    {
        super(calculation);
    }
}

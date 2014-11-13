package gov.va.med.srcalc.domain.workflow;

import gov.va.med.srcalc.domain.Calculation;

/**
 * A Calculation for which the variables have been entered and the outcome(s)
 * calculated.
 */
public class CalculatedCalculation extends CalculationWorkflow
{
    public CalculatedCalculation(final Calculation calculation)
    {
        super(calculation);
    }
}

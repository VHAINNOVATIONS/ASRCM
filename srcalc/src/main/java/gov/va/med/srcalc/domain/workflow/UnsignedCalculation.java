package gov.va.med.srcalc.domain.workflow;

import gov.va.med.srcalc.domain.Calculation;

/**
 * A Calculation for which the variables have been entered and the outcome(s)
 * calculated, but has not been signed yet.
 */
public class UnsignedCalculation extends CalculationWorkflow
{
    public UnsignedCalculation(final Calculation calculation)
    {
        super(calculation);
    }
}

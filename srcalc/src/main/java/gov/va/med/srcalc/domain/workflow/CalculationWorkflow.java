package gov.va.med.srcalc.domain.workflow;

import gov.va.med.srcalc.domain.Calculation;

/**
 * <p>A particular workflow state of a Calculation.</p>
 * 
 * <p>Subclasses provide supporting data for moving to the next step of the workflow.
 * For example, {@link NewCalculation} specifies the list of possible specialties.</p>
 */
public abstract class CalculationWorkflow
{
    private final Calculation fCalculation;
    
    public CalculationWorkflow(final Calculation calculation)
    {
        fCalculation = calculation;
    }
    
    public Calculation getCalculation()
    {
        return fCalculation;
    }
}

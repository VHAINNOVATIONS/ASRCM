package gov.va.med.srcalc.domain.workflow;

import java.io.Serializable;

import gov.va.med.srcalc.domain.calculation.Calculation;

/**
 * <p>A particular workflow state of a Calculation.</p>
 * 
 * <p>Subclasses provide supporting data for moving to the next step of the workflow.
 * For example, {@link NewCalculation} specifies the list of possible specialties.</p>
 */
public abstract class CalculationWorkflow implements Serializable
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

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

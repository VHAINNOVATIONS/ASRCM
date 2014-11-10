package gov.va.med.srcalc.domain.workflow;

import java.util.List;

import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.variable.Variable;

/**
 * A Calculation for which the risk model has been selected.
 */
public class SelectedCalculation extends CalculationWorkflow
{
    public SelectedCalculation(Calculation calculation)
    {
        super(calculation);
    }
    
    /**
     * Returns the variables for the Calculation. Note that this is only
     * appropriate for a SelectedCalculation because a {@link NewCalculation}
     * does not yet have a Specialty assigned.
     */
    public List<Variable> getVariables()
    {
        return getCalculation().getSpecialty().getVariables();
    }
}

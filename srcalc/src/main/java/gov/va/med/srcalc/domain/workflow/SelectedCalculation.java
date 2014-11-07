package gov.va.med.srcalc.domain.workflow;

import java.util.List;

import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.variable.Variable;

/**
 * A Calculation for which the risk model has been selected.
 */
public class SelectedCalculation extends CalculationWorkflow
{
    private final List<Variable> fVariables;
    
    public SelectedCalculation(
            Calculation calculation, List<Variable> variables)
    {
        super(calculation);
        
        fVariables = variables;
    }
    
    public List<Variable> getVariables()
    {
        return fVariables;
    }
}

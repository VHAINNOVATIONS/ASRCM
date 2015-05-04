package gov.va.med.srcalc.domain.workflow;

import java.util.List;

import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.model.Specialty;

/**
 * Represents a brand-new Calculation. 
 */
public class NewCalculation extends CalculationWorkflow
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    private final List<Specialty> fPossibleSpecialties;
    
    public NewCalculation(
            final Calculation calculation, final List<Specialty> possibleSpecialties)
    {
        super(calculation);
        fPossibleSpecialties = possibleSpecialties;
    }
    
    public List<Specialty> getPossibleSpecialties()
    {
        return fPossibleSpecialties;
    }
    
    @Override
    public String toString()
    {
        return String.format(
                "A brand-new calculation for %s. Will need a specialty. (And procedure?)",
                getCalculation().getPatient());
    }
}

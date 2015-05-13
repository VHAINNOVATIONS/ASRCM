package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs sample instances of {@link Calculation}s and related objects.
 */
public class SampleCalculations
{

    public static Patient dummyPatient(final int dfn)
    {
        final Patient patient = new Patient(dfn, "Zach Smith", "M", 40);
        patient.setBmi(20.0);
        return patient;
    }

    /**
     * Create a new calculation for a dummy patient, set the specialty,
     * and then perform the calculation using the a custom set of values.
     * @return a {@link Calculation} object after the calculation is performed
     * @throws Exception
     */
    public static CalculationResult thoracicResult() throws Exception
    {
    	final Calculation calc = Calculation.forPatient(dummyPatient(1));
    	calc.setSpecialty(SampleModels.thoracicSpecialty());
    	final List<Value> values = new ArrayList<Value>();
    	values.add(new BooleanValue(SampleModels.dnrVariable(), false));
    	values.add(new NumericalValue(SampleModels.ageVariable(), 45.0f));
    	values.add(new MultiSelectValue(SampleModels.functionalStatusVariable(), new MultiSelectOption("Independent")));
    	values.add(new ProcedureValue(SampleModels.procedureVariable(), SampleModels.repairLeftProcedure()));
    	return calc.calculate(values);
    }
    
}

package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.util.MissingValuesException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

/**
 * Constructs sample instances of {@link Calculation}s and related objects.
 */
public class SampleCalculations
{
    /**
     * Returns a sample radiologist VistA user.
     */
    public static VistaPerson radiologistPerson()
    {
        return new VistaPerson("500", "11716",
                "RADIOLOGIST,ONE",
                Optional.of("Physicians (M.D. and D.O.)"));
    }

    public static Patient dummyPatient(final int dfn)
    {
        final Patient patient = new Patient(dfn, "Zach Smith", "M", 40);
        patient.setBmi(new RetrievedValue(20.0, new Date(), ""));
        return patient;
    }

    public static Patient dummyPatientWithLabs(final int dfn)
    {
        final Patient patient = dummyPatient(dfn);
        final Map<String, RetrievedValue> labs = new HashMap<String, RetrievedValue>();
        labs.put("WBC", new RetrievedValue(10.0, new Date(), "x1000/mm^3"));
        labs.put("INR", new RetrievedValue(1.0, new Date(), ""));
        patient.setLabs(labs);
        return patient;
    }

    /**
     * Returns a collection of value values for a thoracic calculation.
     * @return a Map from each Variable to its Value
     */
    public static ImmutableMap<AbstractVariable, Value> thoracicValues()
    {
        final BooleanVariable dnrVar = SampleModels.dnrVariable();
        final NumericalVariable ageVar = SampleModels.ageVariable();
        final MultiSelectVariable fsVar = SampleModels.functionalStatusVariable();
        final ProcedureVariable procVar = SampleModels.procedureVariable();
        try
        {
            return ImmutableMap.of(
                    dnrVar,
                    new BooleanValue(dnrVar, false),
                    ageVar,
                    new NumericalValue(ageVar, 45.0f),
                    fsVar,
                    new MultiSelectValue(fsVar, new MultiSelectOption("Independent")),
                    procVar,
                    new ProcedureValue(procVar, SampleModels.repairLeftProcedure()));
        }
        catch (final InvalidValueException ex)
        {
            throw new RuntimeException("test data had an invalid value", ex);
        }
    }
    
    /**
     * Create a new calculation for a dummy patient, set the specialty,
     * and then perform the calculation using the a custom set of values.
     * @return a realistic CalculationResult
     */
    public static CalculationResult thoracicResult()
    {

        final Calculation calc = Calculation.forPatient(dummyPatient(1));
        calc.setSpecialty(SampleModels.thoracicSpecialty());
        try
        {
            return calc.calculate(thoracicValues().values(), radiologistPerson());
        }
        catch (final MissingValuesException ex)
        {
            throw new RuntimeException("test data did not provide all values", ex);
        }
    }
}

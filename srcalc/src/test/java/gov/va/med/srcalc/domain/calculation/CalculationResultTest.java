package gov.va.med.srcalc.domain.calculation;

import static org.junit.Assert.*;

import java.util.HashMap;

import gov.va.med.srcalc.domain.model.BooleanVariable;
import gov.va.med.srcalc.domain.model.SampleModels;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Tests the {@link CalculationResult} class.
 */
public class CalculationResultTest
{
    private final static String NOTE_BODY = String.format("Specialty = Thoracic%n%n"
                + "Procedure = 26546 - Repair left hand - you know, the thing with fingers (10.06)%n%n"
                + "Results%nThoracic 30-day mortality estimate = 100.0%%%n%n"
                + "Calculation Inputs%nAge = 45.0%nDNR = No%nFunctional Status = Independent%n");
    private final static String NOTE_BODY_PROCEDURE_GROUP = String.format("Specialty = Dummy Specialty%n%n"
            + "Dummy Boolean = No%nDummy Boolean 2 = No%n%n"
            + "Results%nDummy Model = 100.0%%%n%n"
            + "Calculation Inputs%nAge = 50.0%nDNR = No%n");
    
    @Test(expected = NullPointerException.class)
    public final void testNullSpecialty() throws Exception
    {
        new CalculationResult(
                null,
                100,
                DateTime.now(),
                ImmutableSet.<Value>of(),
                ImmutableMap.of("model1", 45.1f));
    }
    
    @Test
    public final void testWithProcedure() throws Exception
    {
        /* Setup */
        final CalculationResult result = SampleCalculations.thoracicResult();
        
        /* Verification */
        
        assertTrue(result.getProcedureValue().isPresent());
        assertEquals(3, result.getNonProcedureValues().size());
        assertEquals(result.buildNoteBody(), NOTE_BODY);

        // Most of signed() is verified in the next test, just test the CPT here.
        final SignedResult signedResult = result.signed();
        final ProcedureValue procedureValue = result.getProcedureValue().get();
        assertEquals(
                procedureValue.getValue().getCptCode(), signedResult.getCptCode().get());
    }
    
    /**
     * Tests a {@link CalculationResult} with no ProcedureValue but with other variables
     * in the Procedure group.
     */
    @Test
    public final void testNoProcedureValue() throws Exception
    {
        /* Setup */
        final DateTime startTimestamp = DateTime.now().minusMinutes(3);
        final BooleanVariable procVar1 = new BooleanVariable(
                "Dummy Boolean", SampleModels.procedureVariableGroup(), "dummy");
        final BooleanVariable procVar2 = new BooleanVariable(
                "Dummy Boolean 2", SampleModels.procedureVariableGroup(), "dummy2");
        final ImmutableSet<Value> values = ImmutableSet.of(
                new BooleanValue(SampleModels.dnrVariable(), false),
                new NumericalValue(SampleModels.ageVariable(), 50.0f),
                new BooleanValue(procVar1, false),
                new BooleanValue(procVar2, false));

        final HistoricalCalculation historical = new HistoricalCalculation(
                "Dummy Specialty",
                "668",
                startTimestamp,
                10,
                // Test an empty provider type.
                Optional.<String>absent());
        final CalculationResult result = new CalculationResult(
                historical,
                100,
                DateTime.now(),
                values,
                ImmutableMap.of("Dummy Model", 1.0f));
        
        /* Verification */
        
        assertEquals(Optional.absent(), result.getProcedureValue());
        assertEquals(values, result.getNonProcedureValues());
        assertEquals(NOTE_BODY_PROCEDURE_GROUP, result.buildNoteBody());

        // Also verify signed().
        final HashMap<String, String> expectedInputs = new HashMap<>();
        for (final Value val : result.getValues())
        {
            expectedInputs.put(val.getVariable().getKey(), val.getValue().toString());
        }
        final DateTime expectedSignatureTimestamp = DateTime.now().withMillisOfSecond(0);
        final SignedResult signedResult = result.signed();

        assertEquals(Optional.<String>absent(), signedResult.getCptCode());
        assertSame(historical, signedResult.getHistoricalCalculation());
        assertEquals(expectedInputs, signedResult.getInputs());
        assertEquals(result.getOutcomes(), signedResult.getOutcomes());
        assertEquals(result.getPatientDfn(), signedResult.getPatientDfn());
        assertEquals(
                expectedSignatureTimestamp.toDate(),
                signedResult.getSignatureTimestamp());
    }
}

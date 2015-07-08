package gov.va.med.srcalc.domain.calculation;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.BooleanVariable;
import gov.va.med.srcalc.domain.model.SampleModels;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

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
                new DateTime(),
                100,
                null,
                ImmutableSet.<Value>of(),
                ImmutableMap.of("model1", 45.1));
    }
    
    @Test
    public final void testWithProcedure() throws Exception
    {
        final CalculationResult result = SampleCalculations.thoracicResult();
        
        assertTrue(result.getProcedureValue().isPresent());
        assertEquals(3, result.getNonProcedureValues().size());
        assertEquals(result.buildNoteBody(), NOTE_BODY);
    }
    
    @Test
    public final void testNoProcedure() throws Exception
    {
        final ImmutableSet<Value> values = ImmutableSet.of(
                new BooleanValue(SampleModels.dnrVariable(), false),
                new NumericalValue(SampleModels.ageVariable(), 50.1f));
        final CalculationResult result = new CalculationResult(
                new DateTime(),
                100,
                SampleModels.thoracicSpecialty().getName(),
                values,
                ImmutableMap.of("model1", 0.3));
        
        assertFalse(result.getProcedureValue().isPresent());
        assertEquals(values, result.getNonProcedureValues());
    }
    
    @Test
    public final void testProcedureGroup() throws Exception
    {
        final ImmutableSet<Value> values = ImmutableSet.of(
                new BooleanValue(SampleModels.dnrVariable(), false),
                new NumericalValue(SampleModels.ageVariable(), 50.0f),
                new BooleanValue(new BooleanVariable(
                        "Dummy Boolean",
                        SampleModels.procedureVariableGroup(),
                        "dummy"), false),
                new BooleanValue(new BooleanVariable(
                        "Dummy Boolean 2",
                        SampleModels.procedureVariableGroup(),
                        "dummy2"), false));
        final CalculationResult result = new CalculationResult(
                new DateTime(),
                100,
                "Dummy Specialty",
                values,
                ImmutableMap.of("Dummy Model", 1.0));
        
        assertEquals(values, result.getNonProcedureValues());
        assertEquals(result.buildNoteBody(), NOTE_BODY_PROCEDURE_GROUP);
    }
}

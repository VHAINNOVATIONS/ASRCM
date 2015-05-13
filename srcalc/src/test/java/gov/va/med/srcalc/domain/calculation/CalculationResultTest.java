package gov.va.med.srcalc.domain.calculation;

import static org.junit.Assert.*;
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
    
}

package gov.va.med.srcalc.domain.calculation;

import static org.junit.Assert.*;

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
    public final void testBuildNoteBody() throws Exception
    {
    	final CalculationResult result = SampleCalculations.thoracicResult();

    	assertEquals(result.buildNoteBody(), NOTE_BODY);
    }
    
    @Test
    public final void testGetValues() throws Exception
    {
    	final CalculationResult result = SampleCalculations.thoracicResult();
        
    	assertEquals(1, result.getProcedureValues().size());
    	assertEquals(3, result.getNonProcedureValues().size());
    }
    
}

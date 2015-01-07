package gov.va.med.srcalc.domain;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.variable.*;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.junit.Test;

public class CalculationTest
{
    protected Patient dummyPatient()
    {
        return new Patient(1, "Zach Smith");
    }

    @Test
    public final void testForPatient()
    {
        final Patient patient = dummyPatient();
        final DateTime testStartDateTime = new DateTime();
        final Calculation c = Calculation.forPatient(patient);
        
        assertTrue("start date not in the past",
                // DateTime has millisecond precision, so the current time may
                // still be the same. Use "less than or equal to".
                c.getStartDateTime().compareTo(new DateTime()) <= 0);
        assertTrue("start date not after test start",
                c.getStartDateTime().compareTo(testStartDateTime) >= 0);
        assertEquals(patient, c.getPatient());
    }
    
    @Test
    public final void testSetValidSpecialty()
    {
        final Specialty thoracicSpecialty = SampleObjects.sampleThoracicSpecialty();
        
        // Create the class under test.
        final Calculation calc = Calculation.forPatient(dummyPatient());
        
        // Behavior verification
        calc.setSpecialty(thoracicSpecialty);
        assertEquals(thoracicSpecialty, calc.getSpecialty());
        // Ensure getVariables() returns what we would expect now.
        assertEquals(thoracicSpecialty.getVariables().size(), calc.getVariables().size());
        assertEquals("Procedure", calc.getVariables().get(0).getDisplayName());
        assertEquals("Age", calc.getVariables().get(1).getDisplayName());
        // And same for getVariableGroups().
        assertEquals(3, calc.getVariableGroups().size());
    }
    
    @Test(expected = IllegalStateException.class)
    public final void testGetVariablesIllegal()
    {
        Calculation.forPatient(dummyPatient()).getVariables();
    }
    
    @Test(expected = IllegalStateException.class)
    public final void testGetVariableGroupsIllegal()
    {
        Calculation.forPatient(dummyPatient()).getVariableGroups();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testCalculateIncompleteValues() throws Exception
    {
        final Specialty thoracicSpecialty = SampleObjects.sampleThoracicSpecialty();
        
        // Create the class under test.
        final Calculation calc = Calculation.forPatient(dummyPatient());
        calc.setSpecialty(thoracicSpecialty);
        
        // Behavior verification
        calc.calculate(Arrays.asList(
                new BooleanValue(SampleObjects.dnrVariable(), true),
                new NumericalValue(SampleObjects.sampleAgeVariable(), 12)));
    }
}

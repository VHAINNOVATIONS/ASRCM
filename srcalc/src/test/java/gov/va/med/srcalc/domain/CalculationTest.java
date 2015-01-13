package gov.va.med.srcalc.domain;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.variable.*;
import static gov.va.med.srcalc.domain.SampleObjects.*;

import java.util.Arrays;
import java.util.List;

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
        assertEquals(thoracicSpecialty.getModelVariables(), calc.getVariables());
        // And same for getVariableGroups().
        assertEquals(3, calc.getVariableGroups().size());
    }
    
    @Test(expected = IllegalStateException.class)
    public final void testGetVariablesIllegal()
    {
        Calculation.forPatient(dummyPatient()).getVariables();
    }
    
    @Test
    public final void testGetVariableGroups()
    {
        // First, build a sample Specialty with known variable references.
        final AbstractVariable procedureVar = sampleProcedureVariable();
        final List<AbstractVariable> procedureVars = Arrays.asList(procedureVar);
        final AbstractVariable ageVar = sampleAgeVariable();
        final AbstractVariable genderVar = sampleGenderVariable();
        final List<AbstractVariable> demographicsVars = Arrays.asList(ageVar, genderVar);
        final Specialty specialty = new Specialty(48, "Cardiac");
        specialty.getModelVariables().addAll(demographicsVars);
        specialty.getModelVariables().addAll(procedureVars);

        final Calculation c = Calculation.forPatient(dummyPatient());
        c.setSpecialty(specialty);
        
        // Now, build the expected List of PopulatedVariableGroups.
        final List<PopulatedVariableGroup> list = Arrays.asList(
                new PopulatedVariableGroup(procedureVars),
                new PopulatedVariableGroup(demographicsVars));
        
        // And finally, verify expected behavior. Note that Variables do not
        // override equals() so this only works because the returned list should
        // use the same variable references.
        assertEquals(list, c.getVariableGroups());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public final void testGetVariableGroupsImmutable()
    {
        final Calculation c = Calculation.forPatient(dummyPatient());
        c.setSpecialty(sampleThoracicSpecialty());
        c.getVariableGroups().remove(0);
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

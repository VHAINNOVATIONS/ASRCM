package gov.va.med.srcalc.domain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.va.med.srcalc.domain.model.DerivedTerm;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.util.MissingValuesException;
import static gov.va.med.srcalc.domain.SampleObjects.*;

import java.util.*;

import org.joda.time.DateTime;
import org.junit.Test;

public class CalculationTest
{
    
    @Test
    public final void testForPatient()
    {
        final Patient patient = SampleObjects.dummyPatient(1);
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
        final Calculation calc = Calculation.forPatient(SampleObjects.dummyPatient(1));
        
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
        Calculation.forPatient(SampleObjects.dummyPatient(1)).getVariables();
    }
    
    @Test
    public final void testGetVariableGroups()
    {
        // First, build a sample Specialty with known variable references.
        final AbstractVariable procedureVar = sampleProcedureVariable();
        final AbstractVariable ageVar = sampleAgeVariable();
        final AbstractVariable genderVar = sampleGenderVariable();
        final RiskModel model = SampleObjects.makeSampleRiskModel(
                "model", new HashSet<DerivedTerm>(), procedureVar, ageVar, genderVar);
        final Specialty specialty = new Specialty(48, "Cardiac");
        specialty.getRiskModels().add(model);

        final Calculation c = Calculation.forPatient(SampleObjects.dummyPatient(1));
        c.setSpecialty(specialty);
        
        // Now, build the expected List of PopulatedVariableGroups.
        final List<PopulatedVariableGroup> list = Arrays.asList(
                new PopulatedVariableGroup(Arrays.asList(procedureVar)),
                new PopulatedVariableGroup(Arrays.asList(ageVar, genderVar)));
        
        // And finally, verify expected behavior. Note that Variables do not
        // override equals() so this only works because the returned list should
        // use the same variable references.
        assertEquals(list, c.getVariableGroups());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public final void testGetVariableGroupsImmutable()
    {
        final Calculation c = Calculation.forPatient(SampleObjects.dummyPatient(1));
        c.setSpecialty(sampleThoracicSpecialty());
        c.getVariableGroups().remove(0);
    }
    
    @Test(expected = IllegalStateException.class)
    public final void testGetVariableGroupsIllegal()
    {
        Calculation.forPatient(SampleObjects.dummyPatient(1)).getVariableGroups();
    }
    
    /**
     * Tests running two dummy risk models.
     * @throws MissingValuesException 
     */
    @Test
    public final void testCalculate() throws MissingValuesException
    {
        // Setup
        // we don't actually need any values in here:
        final List<Value> values = Collections.emptyList(); 
        // Create a dummy specialty with two risk models.
        final Specialty s = sampleThoracicSpecialty();
        s.getRiskModels().clear();
        final RiskModel dummyModel1 = mock(RiskModel.class);
        when(dummyModel1.getDisplayName()).thenReturn("model1");
        when(dummyModel1.calculate(values)).thenReturn(55.3);
        s.getRiskModels().add(dummyModel1);
        final RiskModel dummyModel2 = mock(RiskModel.class);
        when(dummyModel2.getDisplayName()).thenReturn("model2");
        when(dummyModel2.calculate(values)).thenReturn(22.3);
        s.getRiskModels().add(dummyModel2);
        final Calculation c = Calculation.forPatient(SampleObjects.dummyPatient(1));
        c.setSpecialty(s);
        
        // Behavior verification
        final TreeMap<String, Double> expectedOutcomes = new TreeMap<>();
        expectedOutcomes.put("model1", 55.3);
        expectedOutcomes.put("model2", 22.3);
        assertEquals(expectedOutcomes, c.calculate(values));
        assertEquals(expectedOutcomes, c.getOutcomes());
    }

    @Test(expected = MissingValuesException.class)
    public final void testCalculateIncompleteValues() throws Exception
    {
        final Specialty thoracicSpecialty = SampleObjects.sampleThoracicSpecialty();
        
        // Create the class under test.
        final Calculation calc = Calculation.forPatient(SampleObjects.dummyPatient(1));
        calc.setSpecialty(thoracicSpecialty);
        
        // Behavior verification
        calc.calculate(Arrays.asList(
                new BooleanValue(SampleObjects.dnrVariable(), true),
                new NumericalValue(SampleObjects.sampleAgeVariable(), 12)));
    }
}

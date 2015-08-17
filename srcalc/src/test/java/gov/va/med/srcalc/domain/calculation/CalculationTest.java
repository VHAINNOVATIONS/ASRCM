package gov.va.med.srcalc.domain.calculation;

import static gov.va.med.srcalc.test.util.TestHelpers.assertWithinDelta;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.model.*;

import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Tests the {@link Calculation} class.
 */
public class CalculationTest
{
    @Test
    public final void testForPatient()
    {
        final Patient patient = SampleCalculations.dummyPatient(1);
        final DateTime testStartDateTime = DateTime.now();
        final Calculation c = Calculation.forPatient(patient);
        
        assertTrue("start date should be in the past",
                // DateTime has millisecond precision, so the current time may
                // still be the same. Use "less than or equal to".
                c.getStartDateTime().compareTo(DateTime.now()) <= 0);
        assertTrue("start date should be after test start",
                c.getStartDateTime().compareTo(testStartDateTime) >= 0);
        assertEquals(patient, c.getPatient());
    }
    
    @Test
    public final void testSetValidSpecialty()
    {
        final Specialty thoracicSpecialty = SampleModels.thoracicSpecialty();
        
        // Create the class under test.
        final Calculation calc = Calculation.forPatient(SampleCalculations.dummyPatient(1));
        
        // Behavior verification
        calc.setSpecialty(thoracicSpecialty);
        assertEquals(thoracicSpecialty, calc.getSpecialty());
        // Ensure getVariables() returns what we would expect now.
        assertEquals(thoracicSpecialty.getModelVariables(), calc.getVariables());
    }
    
    @Test(expected = IllegalStateException.class)
    public final void testGetVariablesIllegal()
    {
        Calculation.forPatient(SampleCalculations.dummyPatient(1)).getVariables();
    }
    
    @Test(expected = ConfigurationException.class)
    public final void testMultipleProcedureVariables()
    {
        // Create a bad specialty.
        final RiskModel model = SampleModels.thoracicRiskModel();
        final ProcedureVariable v = new ProcedureVariable(
                "dupeProcedure", SampleModels.procedureVariableGroup(), "dupeProcedure");
        model.getProcedureTerms().add(new ProcedureTerm(v, 2.0f));
        final Specialty s = SampleModels.thoracicSpecialty();
        s.getRiskModels().add(model);
        
        final Calculation c = Calculation.forPatient(SampleCalculations.dummyPatient(1));
        c.setSpecialty(s);
        c.getVariables();
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
        final Set<Value> values = Collections.emptySet();
        // Create a dummy specialty with two risk models.
        final Specialty s = SampleModels.thoracicSpecialty();
        s.getRiskModels().clear();
        final RiskModel dummyModel1 = mock(RiskModel.class);
        when(dummyModel1.getDisplayName()).thenReturn("model1");
        when(dummyModel1.calculate(values)).thenReturn(55.3f);
        s.getRiskModels().add(dummyModel1);
        final RiskModel dummyModel2 = mock(RiskModel.class);
        when(dummyModel2.getDisplayName()).thenReturn("model2");
        when(dummyModel2.calculate(values)).thenReturn(22.3f);
        s.getRiskModels().add(dummyModel2);
        final Calculation c = Calculation.forPatient(SampleCalculations.dummyPatient(1));
        c.setSpecialty(s);
        final VistaPerson user = SampleCalculations.radiologistPerson();
        
        // Behavior verification
        final CalculationResult result = c.calculate(values, user);
        // The system millis jump in increments of ~10, so just make sure we're within 50.
        assertWithinDelta(DateTime.now(), result.getResultTime(), new Duration(50));
        assertEquals(c.getPatient().getDfn(), result.getPatientDfn());
        assertEquals(s.getName(), result.getSpecialtyName());
        assertEquals(values, result.getValues());
        final TreeMap<String, Float> expectedOutcomes = new TreeMap<>();
        expectedOutcomes.put("model1", 55.3f);
        expectedOutcomes.put("model2", 22.3f);
        assertEquals(expectedOutcomes, result.getOutcomes());
        
        // Also verify output of getHistoricalCalculation().
        final HistoricalCalculation historical = c.getHistoricalCalculation().get();
        assertEquals(s.getName(), historical.getSpecialtyName());
        assertEquals(user.getStationNumber(), historical.getUserStation());
        assertEquals(
                c.getStartDateTime().withMillisOfSecond(0),
                historical.getStartTimestamp());
        // Assuming that 0 seconds elapse from Calculation.forPatient() to calculate()
        // above.
        assertEquals(0, historical.getSecondsToFirstRun());
        assertEquals(user.getProviderType(), historical.getProviderType());
        assertSame(
                "getHistoricalCalculation() should return the same instance",
                historical, c.getHistoricalCalculation().get());
    }

    @Test(expected = MissingValuesException.class)
    public final void testCalculateIncompleteValues() throws Exception
    {
        final Specialty thoracicSpecialty = SampleModels.thoracicSpecialty();
        final ImmutableList<Value> incompleteValues = ImmutableList.of(
                new BooleanValue(SampleModels.dnrVariable(), true),
                new NumericalValue(SampleModels.ageVariable(), 12));
        
        // Create the class under test.
        final Calculation calc = Calculation.forPatient(SampleCalculations.dummyPatient(1));
        calc.setSpecialty(thoracicSpecialty);
        
        // Behavior verification
        calc.calculate(incompleteValues, SampleCalculations.radiologistPerson());
    }
}

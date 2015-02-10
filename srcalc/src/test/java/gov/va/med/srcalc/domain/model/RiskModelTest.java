package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;

import java.util.*;

import gov.va.med.srcalc.domain.Procedure;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.util.CollectionUtils;

import org.junit.Test;

public class RiskModelTest
{
    @Test
    public final void testGetRequiredVariables()
    {
        // Setup
        final ProcedureVariable procedureVar = SampleObjects.sampleProcedureVariable();
        final BooleanVariable dnrVar = SampleObjects.dnrVariable();
        final NumericalVariable ageVar = SampleObjects.sampleAgeVariable();
        final DiscreteNumericalVariable wbcVar = SampleObjects.wbcVariable();
        final MultiSelectVariable fsVar = SampleObjects.functionalStatusVariable();
        final RiskModel model = SampleObjects.makeSampleRiskModel(
                "Thoracic 30-day Mortality Estimate (FY2013)",
                procedureVar, dnrVar, ageVar, wbcVar, fsVar);
        
        // Behavior verification
        final Set<AbstractVariable> expectedVariables =
                CollectionUtils.unmodifiableSet(procedureVar, dnrVar, ageVar, wbcVar, fsVar);
        assertEquals(expectedVariables, model.getRequiredVariables());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testDisplayNameTooLong()
    {
        final RiskModel model = SampleObjects.sampleThoracicRiskModel();
        model.setDisplayName(
                // 81 characters
                "01234567890123456789012345678901234567890123456789012345678901234567890123456789X");
    }
    
    @Test
    public final void testToString()
    {
        final RiskModel model = SampleObjects.sampleThoracicRiskModel();
        
        assertEquals(
                "RiskModel \"Thoracic 30-day mortality estimate\" with 4 terms",
                model.toString());
    }
    
    @Test
    public final void testCalculate() throws Exception
    {
        // Setup
        final ProcedureVariable procedureVar = SampleObjects.sampleProcedureVariable();
        final BooleanVariable dnrVar = SampleObjects.dnrVariable();
        final NumericalVariable ageVar = SampleObjects.sampleAgeVariable();
        final DiscreteNumericalVariable wbcVar = SampleObjects.wbcVariable();
        final MultiSelectVariable fsVar = SampleObjects.functionalStatusVariable();
        final RiskModel model = SampleObjects.makeSampleRiskModel(
                "Thoracic 30-day Mortality Estimate (FY2013)",
                procedureVar, dnrVar, ageVar, wbcVar, fsVar);

        final Procedure selectedProcedure = procedureVar.getProcedures().get(1);
        final float age = 26;
        final List<Value> values = Arrays.asList(
                procedureVar.makeValue(selectedProcedure),
                dnrVar.makeValue(true),
                ageVar.makeValue(age),
                wbcVar.makeValue(25.0f),
                fsVar.makeValue(fsVar.getOptions().get(2)));
        
        // Behavior verification
        double expectedSum =
                1.0f * selectedProcedure.getRvu() +
                2.0f * age +
                3.0f +
                4.0f +
                5.0f;
        double expectedExp = Math.exp(expectedSum);
        double expectedResult = expectedExp / (1 + expectedExp);
        assertEquals(expectedResult, model.calculate(values), 0.01f);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testCalculateIncompleteValues() throws Exception
    {
        final RiskModel model = SampleObjects.sampleThoracicRiskModel();
        
        model.calculate(Arrays.asList(
                new BooleanValue(SampleObjects.dnrVariable(), true),
                new NumericalValue(SampleObjects.sampleAgeVariable(), 12)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testCalculateDuplicateValues() throws Exception
    {
        final BooleanVariable dnrVar = SampleObjects.dnrVariable();
        final RiskModel model = SampleObjects.makeSampleRiskModel(
                "model", dnrVar);
        
        model.calculate(Arrays.<Value>asList(
                dnrVar.makeValue(true), dnrVar.makeValue(false)));
    }
}

package gov.va.med.srcalc.domain.model;

import static org.junit.Assert.*;

import java.util.Set;

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
    
}

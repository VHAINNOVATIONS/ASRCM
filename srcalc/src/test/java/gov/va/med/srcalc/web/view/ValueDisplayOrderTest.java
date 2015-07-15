package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.test.util.TestHelpers;

import org.junit.Test;

/**
 * Tests the {@link ValueDisplayOrder} class.
 */
public class ValueDisplayOrderTest
{
    @Test
    public final void testCompare() throws Exception
    {
        final ProcedureValue procedureValue = new ProcedureValue(
                SampleModels.procedureVariable(), SampleModels.repairLeftProcedure());
        final BooleanValue dnrValue = new BooleanValue(
                SampleModels.dnrVariable(), false);
        final NumericalValue ageValue = new NumericalValue(
                SampleModels.ageVariable(), 42.0f);
        
        TestHelpers.verifyComparatorContract(
                new ValueDisplayOrder(), procedureValue, ageValue, dnrValue);
    }
    
}

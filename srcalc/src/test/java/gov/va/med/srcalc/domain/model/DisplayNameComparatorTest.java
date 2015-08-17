package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.test.util.TestHelpers;

import org.junit.Test;

/**
 * <p>Tests the {@link DisplayNameComparator} class.</p>
 * 
 * <p>Note that these test cases use BDD-style names "should {do something}".</p>
 */
public class DisplayNameComparatorTest
{
    @Test
    public final void shouldSortByDisplayName()
    {
        final BooleanVariable lesser = new BooleanVariable(
                "A Boolean", SampleModels.recentClinicalVariableGroup(), "lesser");
        // Intentionally use the same display name to test consistency with equals.
        final BooleanVariable middle = new BooleanVariable(
                "A Boolean", SampleModels.recentClinicalVariableGroup(), "middle");
        final NumericalVariable greater = new NumericalVariable(
                "A Numerical", SampleModels.demographicsVariableGroup(), "greater");
        
        TestHelpers.verifyComparatorContract(
                new DisplayNameComparator(), lesser, middle, greater);
    }
    
}

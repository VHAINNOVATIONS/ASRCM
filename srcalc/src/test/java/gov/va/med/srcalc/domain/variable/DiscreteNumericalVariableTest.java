package gov.va.med.srcalc.domain.variable;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class DiscreteNumericalVariableTest
{
    @Test
    public final void testCategoryEquals()
    {
        EqualsVerifier.forClass(DiscreteNumericalVariable.Category.class)
            // The public interface is immutable and does not permit null fields.
            .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS)
            .verify();
    }
    
}

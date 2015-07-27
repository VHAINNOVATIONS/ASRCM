package gov.va.med.srcalc.domain.calculation;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

/**
 * Tests the {@link SignedResult} class.
 */
public class SignedResultTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(SignedResult.class).verify();
    }
    
    /**
     * Tests that {@link SignedResult#toString()} contains some basic information
     * to make it useful.
     */
    @Test
    public final void testToString()
    {
        final int patientDfn = 100;
        final String specialty = "Urology";
        final String cptCode = "10005";

        final SignedResult result = new SignedResult(
                patientDfn,
                specialty,
                Optional.of(cptCode),
                new DateTime(),
                new DateTime(),
                ImmutableMap.<String, Float>of());
        
        assertThat(result.toString(), allOf(
                containsString(specialty),
                containsString(cptCode),
                containsString(String.valueOf(patientDfn))));
    }
    
}

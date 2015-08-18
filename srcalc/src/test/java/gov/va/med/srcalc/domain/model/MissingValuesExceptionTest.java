package gov.va.med.srcalc.domain.model;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

/**
 * <p>Unit tests for {@link MissingValuesException}.</p>
 * 
 * <p>Note that these test cases use BDD-style names "should {do something}".</p>
 */
public class MissingValuesExceptionTest
{
    @Test(expected = IllegalArgumentException.class)
    public final void shouldThrowIllegalArgumentExceptionForEmptySet()
    {
        final ImmutableSet<Variable> emptySet = ImmutableSet.of();
        new MissingValuesException(emptySet);
    }
    
    @Test
    public final void shouldReturnTheProvidedSet()
    {
        final Variable var1 = SampleModels.ageVariable();
        final Variable var2 = SampleModels.genderVariable();
        final ImmutableSet<Variable> missingVars = ImmutableSet.of(var1, var2);
        final MissingValuesException ex = new MissingValuesException(missingVars);
        
        /* Verification */
        assertEquals(missingVars, ex.getMissingVariables());
    }
    
    @Test
    public final void shouldReturnTheSpecifiedMessage()
    {
        final Variable var1 = SampleModels.ageVariable();
        final Variable var2 = SampleModels.genderVariable();
        final MissingValuesException ex = new MissingValuesException(
                ImmutableSet.of(var1, var2));
        
        /* Verification */
        assertThat(ex.getMessage(), allOf(
                containsString(var1.getKey()), containsString(var2.getKey())));
        // Ensure that getLocalizedMessage() does what we expect.
        assertEquals(ex.getMessage(), ex.getLocalizedMessage());
    }
    
}

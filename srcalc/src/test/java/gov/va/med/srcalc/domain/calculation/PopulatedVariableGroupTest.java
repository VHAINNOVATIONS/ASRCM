package gov.va.med.srcalc.domain.calculation;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.calculation.PopulatedDisplayGroup;
import gov.va.med.srcalc.domain.model.*;

import java.util.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

/**
 * Tests the {@link PopulatedDisplayGroup} class.
 */
public class PopulatedVariableGroupTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(PopulatedDisplayGroup.class)
            .suppress(Warning.NULL_FIELDS)  // variables may not be null
            .verify();
    }

    @Test
    public final void testToString()
    {
        final List<AbstractVariable> variables = Arrays.asList(
                SampleModels.ageVariable(),
                SampleModels.genderVariable());
        final PopulatedDisplayGroup group = new PopulatedDisplayGroup(variables);
        
        assertEquals(
                "Variable Group 'Demographics' with variables [Age, Gender]",
                group.toString());
    }
    
    @Test
    public final void testBasic()
    {
        final AbstractVariable genderVar = SampleModels.genderVariable();
        final AbstractVariable ageVar = SampleModels.ageVariable();
        final List<AbstractVariable> variables = Arrays.asList(
                ageVar, genderVar);
        final PopulatedDisplayGroup group = new PopulatedDisplayGroup(variables);
        
        assertEquals("Demographics", group.getName());
        assertEquals(variables, group.getVariables());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyVariables()
    {
        new PopulatedDisplayGroup(new ArrayList<Variable>());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testMixedVariables()
    {
        final List<AbstractVariable> variables = Arrays.asList(
                SampleModels.ageVariable(),
                SampleModels.procedureVariable());
        new PopulatedDisplayGroup(variables);
    }
    
}

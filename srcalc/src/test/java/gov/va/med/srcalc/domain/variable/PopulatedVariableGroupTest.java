package gov.va.med.srcalc.domain.variable;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;

import java.util.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class PopulatedVariableGroupTest
{
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(PopulatedVariableGroup.class)
            .suppress(Warning.NULL_FIELDS)  // variables may not be null
            .verify();
    }

    @Test
    public final void testToString()
    {
        final List<AbstractVariable> variables = Arrays.asList(
                SampleObjects.sampleAgeVariable(),
                SampleObjects.sampleGenderVariable());
        final PopulatedVariableGroup group = new PopulatedVariableGroup(variables);
        
        assertEquals(
                "Variable Group 'Demographics' with variables [Age, Gender]",
                group.toString());
    }
    
    @Test
    public final void testGetName()
    {
        final List<AbstractVariable> variables = Arrays.asList(
                SampleObjects.sampleAgeVariable(),
                SampleObjects.sampleGenderVariable());
        final PopulatedVariableGroup group = new PopulatedVariableGroup(variables);
        
        assertEquals("Demographics", group.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyVariables()
    {
        new PopulatedVariableGroup(new ArrayList<Variable>());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testMixedVariables()
    {
        final List<AbstractVariable> variables = Arrays.asList(
                SampleObjects.sampleAgeVariable(),
                SampleObjects.sampleProcedureVariable());
        new PopulatedVariableGroup(variables);
    }
    
}

package gov.va.med.srcalc.domain.calculation;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.calculation.PopulatedVariableGroup;
import gov.va.med.srcalc.domain.model.*;

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
                SampleModels.ageVariable(),
                SampleModels.genderVariable());
        final PopulatedVariableGroup group = new PopulatedVariableGroup(variables);
        
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
        final PopulatedVariableGroup group = new PopulatedVariableGroup(variables);
        
        assertEquals("Demographics", group.getName());
        assertEquals(variables, group.getVariables());
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
                SampleModels.ageVariable(),
                SampleModels.procedureVariable());
        new PopulatedVariableGroup(variables);
    }
    
}

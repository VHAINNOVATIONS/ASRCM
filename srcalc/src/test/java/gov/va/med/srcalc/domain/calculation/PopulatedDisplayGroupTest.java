package gov.va.med.srcalc.domain.calculation;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.HealthFactor;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.web.view.PopulatedDisplayGroup;

import java.util.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * Tests the {@link PopulatedDisplayGroup} class.
 */
public class PopulatedDisplayGroupTest
{
    @Test
    public final void testEquals()
    {
        // variables may not be null, which causes display items to be non-null
        EqualsVerifier.forClass(PopulatedDisplayGroup.class)
            .suppress(Warning.NULL_FIELDS)
            .verify();
    }

    @Test
    public final void testToString()
    {
        final List<AbstractVariable> variables = Arrays.asList(
                SampleModels.ageVariable(),
                SampleModels.genderVariable());
        final Patient patient = SampleCalculations.dummyPatient(1);
        patient.getHealthFactors().add(new HealthFactor(LocalDate.now(), "Dummy health factor"));
        final PopulatedDisplayGroup group = new PopulatedDisplayGroup(variables, patient);
        final PopulatedDisplayGroup group2 = new PopulatedDisplayGroup(
                Arrays.asList(SampleModels.functionalStatusVariable()), patient);
        assertEquals(
                "Display Group 'Demographics' with display items [Age, Gender]",
                group.toString());
        // Make sure the toString works with reference information too.
        assertEquals("Display Group 'Clinical Conditions or Diseases - Recent' with display items [Health Factors, Functional Status]",
                group2.toString());
    }
    
    @Test
    public final void testBasic()
    {
        final AbstractVariable genderVar = SampleModels.genderVariable();
        final AbstractVariable ageVar = SampleModels.ageVariable();
        final List<AbstractVariable> variables = Arrays.asList(
                ageVar, genderVar);
        final PopulatedDisplayGroup group = new PopulatedDisplayGroup(variables, SampleCalculations.dummyPatient(1));
        
        assertEquals("Demographics", group.getName());
        assertEquals(variables, group.getVariables());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyVariables()
    {
        new PopulatedDisplayGroup(new ArrayList<Variable>(), SampleCalculations.dummyPatient(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testMixedVariables()
    {
        final List<AbstractVariable> variables = Arrays.asList(
                SampleModels.ageVariable(),
                SampleModels.procedureVariable());
        new PopulatedDisplayGroup(variables, SampleCalculations.dummyPatient(1));
    }
}

package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.HealthFactor;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.domain.calculation.ValueRetriever;
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
        final Patient patient = SampleCalculations.dummyPatient(1);
        patient.getHealthFactors().add(new HealthFactor(LocalDate.now(), "Dummy health factor"));
        final AbstractVariable fsVar = SampleModels.functionalStatusVariable();
        fsVar.setRetriever(ValueRetriever.FUNCTIONAL_STATUS);
        // Test a PopulatedDisplayGroup with variables and reference information
        final PopulatedDisplayGroup group = new PopulatedDisplayGroup(
                Arrays.asList(fsVar), patient);
        final List<PopulatedDisplayGroup> groupList = new ArrayList<PopulatedDisplayGroup>();
        groupList.add(group);
        
        ReferenceInfoAdder.addRefInfo(groupList, patient);
        assertEquals("Display Group 'Clinical Conditions or Diseases - Recent' with display items [Health Factors, Functional Status]",
                group.toString());
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

    @Test(expected = IndexOutOfBoundsException.class)
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
    
    @Test
    public final void testOnlyReferenceInfo()
    {
        final Patient patient = SampleCalculations.dummyPatient(1);
        // Test a PopulatedDisplayGroup with only reference information
        final PopulatedDisplayGroup group = new PopulatedDisplayGroup(
                SampleModels.medicationsVariableGroup(), patient);
        assertEquals(Collections.<Variable>emptyList(), group.getVariables());
        assertEquals(0, group.getDisplayItems().size());
        final List<PopulatedDisplayGroup> groupList = new ArrayList<PopulatedDisplayGroup>();
        groupList.add(group);
        ReferenceInfoAdder.addRefInfo(groupList, patient);
        assertEquals(1, group.getDisplayItems().size());
    }
}

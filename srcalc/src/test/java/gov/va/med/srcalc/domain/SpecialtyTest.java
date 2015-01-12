package gov.va.med.srcalc.domain;

import static org.junit.Assert.*;

import java.util.*;

import gov.va.med.srcalc.domain.variable.*;
import static gov.va.med.srcalc.domain.SampleObjects.*;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class SpecialtyTest
{
    @Test
    public final void testToString()
    {
        final String name = "Jack-of-all-trades";
        assertEquals(name, new Specialty(59, name).toString());
    }
    
    @Test
    public final void testEqualsObject()
    {
        EqualsVerifier.forClass(Specialty.class).verify();
    }
    
    @Test
    public final void testGetVariableGroups()
    {
        // First, build a sample Specialty with known variable references.
        final AbstractVariable procedureVar = sampleProcedureVariable();
        final List<AbstractVariable> procedureVars = Arrays.asList(procedureVar);
        final AbstractVariable ageVar = sampleAgeVariable();
        final AbstractVariable genderVar = sampleGenderVariable();
        final List<AbstractVariable> demographicsVars = Arrays.asList(ageVar, genderVar);
        final Specialty specialty = new Specialty(48, "Cardiac");
        specialty.getVariables().addAll(demographicsVars);
        specialty.getVariables().addAll(procedureVars);
        
        // Now, build the expected List of PopulatedVariableGroups.
        final List<PopulatedVariableGroup> list = Arrays.asList(
                new PopulatedVariableGroup(procedureVars),
                new PopulatedVariableGroup(demographicsVars));
        
        // And finally, verify expected behavior. Note that Variables do not
        // override equals() so this only works because the returned list should
        // use the same variable references.
        assertEquals(list, specialty.getVariableGroups());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public final void testGetVariableGroupsImmutable()
    {
        sampleThoracicSpecialty().getVariableGroups().remove(0);
    }
}

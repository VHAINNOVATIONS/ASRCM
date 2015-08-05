package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;

import java.util.ArrayList;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.domain.model.SampleModels;

import org.junit.Test;

/**
 * Unit tests for the {@link ReferenceInfoAdder} class.
 */
public class ReferenceInfoAdderTest
{
    @Test
    public final void testAddRefInfo()
    {
        final Patient patient = SampleCalculations.dummyPatient(1);
        final ArrayList<PopulatedDisplayGroup> groupList = new ArrayList<>();
        ReferenceInfoAdder.addRefInfo(groupList, patient);
        // Should have added Medications and Clinical Conditions
        assertEquals(2, groupList.size());
        final PopulatedDisplayGroup addedGroup = groupList.get(0);
        // Medications should be first.
        assertEquals(SampleModels.medicationsVariableGroup(), addedGroup.getGroup());
        assertEquals(2, addedGroup.getDisplayItems().size());
    }
    
}

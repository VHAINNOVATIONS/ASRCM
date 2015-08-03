package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.HealthFactor;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.domain.model.VariableGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ReferenceInfoAdder
{
    public static void addRefInfo(final Map<VariableGroup, List<Variable>> map, final List<PopulatedDisplayGroup> groupList,
            final Patient patient)
    {
        // Add reference information for existing groups.
        for(final PopulatedDisplayGroup populatedGroup: groupList)
        {
            addReferenceInfo(populatedGroup.getGroup(), populatedGroup, patient);
        }
        final VariableGroup medicationsGroup = new VariableGroup("Medications", 3);
        // Check to see if the any groups are missing because of that group not having variables.
        // Add the group with reference information if it is missing.
        if(!map.containsKey(medicationsGroup))
        {
            final PopulatedDisplayGroup populatedMedGroup = new PopulatedDisplayGroup(medicationsGroup, patient);
            addReferenceInfo(medicationsGroup, populatedMedGroup, patient);
            groupList.add(populatedMedGroup);
        }
        final VariableGroup clinicalConditionsGroup = new VariableGroup("Clinical Conditions or Diseases - Recent", 5);
        if(!map.containsKey(clinicalConditionsGroup))
        {
            final PopulatedDisplayGroup populatedClinicalGroup = new PopulatedDisplayGroup(clinicalConditionsGroup, patient);
            addReferenceInfo(clinicalConditionsGroup, populatedClinicalGroup, patient);
            groupList.add(populatedClinicalGroup);
        }
    }
    
    private static void addReferenceInfo(final VariableGroup group,
            final PopulatedDisplayGroup populatedGroup, final Patient patient)
    {
        // These group names are all well-known and are hard-coded as such.
        if(group.getName().equalsIgnoreCase(VariableGroup.CLINICAL_GROUP))
        {
            final List<String> factorList = new ArrayList<String>();
            // Output the date without the time of day.
            final DateTimeFormatter format = DateTimeFormat.forPattern("MM/dd/yy");
            for(final HealthFactor factor: patient.getHealthFactors())
            {
                factorList.add(String.format("%s %s%n", format.print(factor.getDate()), factor.getName()));
            }
            final DisplayItem refInfo = new ReferenceItem("Health Factors", group, factorList);
            populatedGroup.getDisplayItems().add(0, refInfo);
        }
        else if(group.getName().equalsIgnoreCase(VariableGroup.MEDICATIONS_GROUP))
        {
            populatedGroup.getDisplayItems().add(0, 
                    new ReferenceItem("Active Medications", group, patient.getActiveMedications()));
        }
    }
}

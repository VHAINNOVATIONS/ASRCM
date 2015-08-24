package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.HealthFactor;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.model.VariableGroup;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.ImmutableList;

/**
 * <p>A class used to add reference information to existing PopulatedDisplayGroups and create
 * PopulatedDisplayGroups that do not exist, in order to store reference information to display
 * for the user.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class ReferenceInfoAdder
{
    private static final String REMOTE_MEDICATIONS_URL = "http://www.google.com/";
    
    /**
     * Adds reference information for all of the given {@link PopulatedDisplayGroup}s and adds any groups
     * that are missing in order to include their reference information as well.
     * @param groupList a list of all previously existing PopulatedDisplayGroup
     * @param patient the patient to add reference information from
     */
    public static void addRefInfo(final List<PopulatedDisplayGroup> groupList, final Patient patient)
    {
        // Add reference information for existing groups.
        for(final PopulatedDisplayGroup populatedGroup: groupList)
        {
            addReferenceInfo(populatedGroup.getGroup(), populatedGroup, patient);
        }
        final VariableGroup medicationsGroup = new VariableGroup(VariableGroup.MEDICATIONS_GROUP, 3);
        // Check to see if the any groups are missing because of that group not having variables.
        // Add the group with reference information if it is missing.
        if(!doesGroupExist(groupList, medicationsGroup))
        {
            final PopulatedDisplayGroup populatedMedGroup =
                    new PopulatedDisplayGroup(medicationsGroup, patient);
            addReferenceInfo(medicationsGroup, populatedMedGroup, patient);
            groupList.add(populatedMedGroup);
        }
        final VariableGroup clinicalConditionsGroup = new VariableGroup(VariableGroup.CLINICAL_GROUP, 5);
        if(!doesGroupExist(groupList,clinicalConditionsGroup))
        {
            final PopulatedDisplayGroup populatedClinicalGroup =
                    new PopulatedDisplayGroup(clinicalConditionsGroup, patient);
            addReferenceInfo(clinicalConditionsGroup, populatedClinicalGroup, patient);
            groupList.add(populatedClinicalGroup);
        }
    }
    
    private static boolean doesGroupExist(
            final List<PopulatedDisplayGroup> groupList, final VariableGroup varGroup)
    {
        for(final PopulatedDisplayGroup currentGroup: groupList)
        {
            if(currentGroup.getName().equals(varGroup.getName()))
            {
                return true;
            }
        }
        return false;
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
            final String remoteHtml = String.format(
                    "<a href=\"%s\" target=\"_blank\">Please click here to view remote medications.</a>",
                    REMOTE_MEDICATIONS_URL);
            populatedGroup.getDisplayItems().add(0, 
                    new ReferenceItem("Active Medications", group, patient.getActiveMedications()));
            populatedGroup.getDisplayItems().add(
                    1,
                    new ReferenceItem("Remote Medications", group, ImmutableList.of(remoteHtml)));
        }
    }
}

package gov.va.med.srcalc.vista;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.Patient;

/**
 * <p>Data Access Object for VistA Patients.</p>
 * 
 * <p>This is an interface to allow for easy mocking. See {@link
 * RpcVistaPatientDao} for the "real" implementation.</p>
 */
public interface VistaPatientDao
{
    /**
     * Enumerates the possible responses from VistA for saving a risk calculation note.
     */
    enum SaveNoteCode
    {
        SUCCESS("Success"),
        INVALID_SIGNATURE("Invalid Electronic Signature Code");
        
        private final String fDescription;
        
        SaveNoteCode(final String description)
        {
            fDescription = description;
        }
        
        public String getDescription()
        {
            return fDescription;
        }
    }
    
    /**
     * This is a list of the only health factors that should be displayed for the users.
     * This list was created by the VA.
     */
    public static final Set<String> HEALTH_FACTORS_SET = ImmutableSet.of("ALCOHOL - TREATMENT REFERRAL", "ALCOHOL USE",
        "ANTI-DEPRESSANT TREATMENT", "BINGE DRINKING", "CURRENT F/U OR RX FOR DEPRESSION",
        "DECLINES HOMELESS REFERRAL", "DEPRESSION ASSESS NEGATIVE (NOT MDD)",
        "DEPRESSION ASSESS POSITIVE (MDD)", "DEPRESSION TO BE MANAGED IN PC",
        "GEC HOMELESS", "GEC HOMELESS SHELTER", "HISTORY OF AN ALCOHOL PROBLEM",
        "HOMELESSNESS SCREENING", "NON-DRINKER (NO ALCOHOL FOR >1 YR)",
        "ONS AA MEDICATIONS-ANTIDEPRESSANTS", "ONS AA MH TRIGGER ID-BEING HOMELESS",
        "ONS RA MEDICATIONS-ANTIDEPRESSANTS", "OUTSIDE EVAL/TREATMENT FOR DEPRESSION",
        "PALLI CONSULT ALCOHOL MISUSE NO", "PALLI CONSULT ALCOHOL MISUSE YES",
        "PC DEPRESSION SCREEN NEGATIVE", "PC DEPRESSION SCREEN POSITIVE",
        "REFER FOR ALCOHOL TREATMENT", "REFERRED TO HOMELESS PROGRAM",
        "REFUSED DEPRESSION RX/INTERVENTION", "REFUSES MH REFERRAL FOR DEPRESSION");

    /**
     * The maximum amount of characters allowed on a line before wrapping the line
     * onto a new line.
     */
    public static final int MAX_LINE_LENGTH = 80;
    
    /**
     * Loads a Patient from VistA given his/her DFN. This includes the patient's
     * vitals and available lab measurements.
     * @throws DataAccessException if communication with VistA failed
     */
    public Patient getPatient(final int dfn);
    
    /**
     * Saves the finished calculation to VistA, given the patient, electronic signature, 
     * and the note body. Each line of the noteBody is wrapped at {@link VistaPatientDao#MAX_LINE_LENGTH}
     * characters so that the note is easily visible in CPRS without horizontal scrolling.
     * @param patientDfn the DFN of the patient
     * @param noteBody 
     * @return one of the {@link SaveNoteCode} return codes
     */
    public SaveNoteCode saveRiskCalculationNote(final int patientDfn, final String electronicSignature, String noteBody);
    
}

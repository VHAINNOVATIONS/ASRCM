package gov.va.med.srcalc.vista;

public enum RemoteProcedure
{
    /**
     * Returns information about the current user.
     */
    GET_USER("SR ASRC USER"),
    
    /**
     * Returns information about the given patient.
     */
    GET_PATIENT("SR ASRC PATIENT"),
    
    /**
     * Returns the most recent vitals on the patient.
     */
    GET_RECENT_VITALS("GMV LATEST VM"),
    
    /**
     * Returns a vital from the given patient with the specified date range.
     */
    GET_VITAL("GMV EXTRACT REC"),
    
    /**
     * Submits a completed calculation as a note on the patient's records.
     */
    SAVE_PROGRESS_NOTE("SR ASRC PROGRESS NOTE");
    
    private final String fProcedureName;
    
    RemoteProcedure(final String procedureName)
    {
        fProcedureName = procedureName;
    }
    
    /**
     * Returns the actual name of the Remote Procedure in VistA.
     */
    public String getProcedureName()
    {
        return fProcedureName;
    }
}

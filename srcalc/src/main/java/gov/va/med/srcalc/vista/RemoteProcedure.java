package gov.va.med.srcalc.vista;

public enum RemoteProcedure
{
    /**
     * Returns information about the current user.
     */
    GET_USER("SR ASRC USER"),
    
    /**
     * Returns information about a logged in user from a CCOW login token.
     * 
     * See <a href="http://code.osehra.org/Prod/Visual/files/8994-2877.html">
     * XUS KAAJEE GET USER VIA PROXY</a> for parameter information.
     */
    GET_CCOW_USER("XUS KAAJEE GET USER VIA PROXY"),
    
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
    GET_VITAL("GMV EXTRACT REC");
    
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

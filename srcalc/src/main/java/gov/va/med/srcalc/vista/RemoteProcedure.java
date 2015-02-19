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
    GET_PATIENT("SR ASRC PATIENT");
    
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

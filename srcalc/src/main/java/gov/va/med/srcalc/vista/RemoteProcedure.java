package gov.va.med.srcalc.vista;

/**
 * Enumerates all VistA Remote Procedures which this application uses.
 */
public enum RemoteProcedure
{
    /**
     * <p>Returns information about the current user.</p>
     * 
     * <p>The returned array contains at least the following elements:</p>
     * 
     * <ol>
     * <li>the user's DUZ</li>
     * <li>the user's name from the NEW PERSON file</li>
     * <li>the user's full name from the name standard file</li>
     * <li>the user's division, as IEN of INSTITUTION file^station name^station number</li>
     * <li>the user's title</li>
     * <li>the user's service/section</li>
     * </ol>
     */
    GET_USER_INFO("XUS GET USER INFO", RpcContext.XUS_SIGNON),

    // Note: there is also XUS KAAJEE GET USER INFO, which returns better information,
    // but I am not sure we could use it in production. XUS GET USER INFO is public-use.
    
    /**
     * <p>Returns information about the user associated with the given CCOW token.</p>
     * 
     * <p>Parameters:</p>
     * 
     * <ol>
     * <li>the client IP address (VistA verifies this address.)</li>
     * <li>the application name for VistA's signon log</li>
     * <li>the actual CCOW token</li>
     * </ol>
     * 
     * <p>The returned array contains at least the following elements:</p>
     * 
     * <ol>
     * <li>the user's DUZ</li>
     * <li>the user's name from the NEW PERSON file</li>
     * <li>the user's full name from the NAME COMPONENTS file</li>
     * </ol>
     * 
     * @see VistaAuthenticator#authenticateViaCcowToken(String, String)
     */
    GET_USER_FROM_CCOW("XUS KAAJEE GET USER VIA PROXY", RpcContext.XUS_KAAJEE_PROXY_LOGON),
    
    /**
     * Returns Person Classes for the current user.
     */
    GET_USER_PERSON_CLASSES("SR ASRC PERSON CLASSES"),
    
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
    SAVE_PROGRESS_NOTE("SR ASRC PROGRESS NOTE"),
    
    /**
     * Saves a Risk Calculation as discrete data to VistA Surgery.
     */
    SAVE_RISK("SR ASRC RISK SAVE"),
    
    /**
     * Returns the most recent result for the given lab name possibilities.
     */
    GET_LABS("SR ASRC LAB RESULTS"),
    
    /**
     * Returns any of the patient's health factors in the last year.
     */
    GET_HEALTH_FACTORS("SR ASRC HEALTH FACTORS"),
    
    /**
     * Returns all VA and non-VA active medications for the patient.
     */
    GET_ACTIVE_MEDICATIONS("SR ASRC ACTIVE MEDS"),

    /**
     * Returns all nursing notes with the "NURSING ADMISSION EVALUATION NOTE" title.
     */
    GET_ADL_STATUS("SR ASRC ADL NOTES"),
    
    /**
     * Returns any nursing notes for the specified patient, that contain the specified substring.
     */
    GET_NOTES_WITH_SUBSTRING("SR ASRC DNR NOTES");

    /**
     * VistA returns this string if {@link #SAVE_PROGRESS_NOTE} succeeds.
     */
    public final static String VALID_SIGNATURE_RETURN =
            "1^Progress note was created and signed successfully.";
    
    /**
     * VistA returns this string if {@link #SAVE_RISK} succeeds.
     */
    public final static String RISK_SAVED_RETURN =
            "1^Record created successfully.";
    
    /**
     * {@link #GET_USER_FROM_CCOW} returns this DUZ if the token was invalid (or
     * associated with a different client IP).
     */
    public final static String BAD_TOKEN_DUZ = "0";
    
    private final RpcContext fRpcContext;
    
    private final String fProcedureName;
    
    /**
     * Constructs an instance with the given attributes.
     */
    private RemoteProcedure(final String procedureName, final RpcContext rpcContext)
    {
        fProcedureName = procedureName;
        fRpcContext = rpcContext;
    }
    
    /**
     * Constructs an instance with the given procedure name and {@link RpcContext#SR_ASRC}
     * as the associated RPC context.
     */
    private RemoteProcedure(final String procedureName)
    {
        this(procedureName, RpcContext.SR_ASRC);
    }
    
    /**
     * Returns the actual name of the Remote Procedure in VistA.
     */
    public String getProcedureName()
    {
        return fProcedureName;
    }
    
    /**
     * Returns the Remote Procedure's associated RPC context. This context should be used
     * when calling the Remote Procedure.
     */
    public RpcContext getRpcContext()
    {
        return fRpcContext;
    }
}

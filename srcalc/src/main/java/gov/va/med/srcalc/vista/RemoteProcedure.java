package gov.va.med.srcalc.vista;

/**
 * Enumerates all VistA Remote Procedures which this application uses. Each enumeration
 * constant is the name of a VistA Remote Procedure with spaces replaced by underscores.
 */
public enum RemoteProcedure
{
    /**
     * <p>Returns information about the current user.</p>
     * 
     * <p>See <a href="http://code.osehra.org/Prod/Visual/files/8994-356.html">the OSEHRA
     * documentation</a> for parameter and return value documentation.</p>
     */
    XUS_GET_USER_INFO("XUS GET USER INFO", RpcContext.XUS_SIGNON),

    // Note: there is also XUS KAAJEE GET USER INFO, which returns better information,
    // but I am not sure we could use it in production. XUS GET USER INFO is public-use.
    
    /**
     * <p>Returns information about the user associated with the given CCOW token.</p>
     * 
     * <p>See <a href="http://code.osehra.org/Prod/Visual/files/8994-2877.html">the OSEHRA
     * documentation</a> for parameter and return value documentation. Note that the CCOW
     * token parameter must actually be {@code encrypt("~~TOK~~" + token)}.</p>
     * 
     * @see VistaAuthenticator#authenticateViaCcowToken(String, String)
     */
    XUS_KAAJEE_GET_USER_VIA_PROXY(
            "XUS KAAJEE GET USER VIA PROXY", RpcContext.XUS_KAAJEE_PROXY_LOGON),
    
    /**
     * <p>Returns Person Classes for the current user.</p>
     * 
     * <p>As this Remote Procedure is defined in the ASRC VistA patch, consult the ASRC
     * Technical Manual for parameter and return value documentation.</p>
     */
    SR_ASRC_PERSON_CLASSES("SR ASRC PERSON CLASSES"),
    
    /**
     * Returns information about the given patient.
     * 
     * <p>As this Remote Procedure is defined in the ASRC VistA patch, consult the ASRC
     * Technical Manual for parameter and return value documentation.</p>
     */
    SR_ASRC_GET_PATIENT("SR ASRC PATIENT"),
    
    /**
     * Returns the most recent vitals on the patient.
     */
    GMV_LATEST_VM("GMV LATEST VM"),
    
    /**
     * Returns a vital from the given patient with the specified date range.
     */
    GMV_EXTRACT_REC("GMV EXTRACT REC"),
    
    /**
     * Submits a completed calculation as a note on the patient's records.
     * 
     * <p>As this Remote Procedure is defined in the ASRC VistA patch, consult the ASRC
     * Technical Manual for parameter and return value documentation.</p>
     */
    SR_ASRC_SAVE_PROGRESS_NOTE("SR ASRC PROGRESS NOTE"),
    
    /**
     * Saves a Risk Calculation as discrete data to VistA Surgery.
     * 
     * <p>As this Remote Procedure is defined in the ASRC VistA patch, consult the ASRC
     * Technical Manual for parameter and return value documentation.</p>
     */
    SR_ASRC_RISK_SAVE("SR ASRC RISK SAVE"),
    
    /**
     * Returns the most recent result for the given lab name possibilities.
     * 
     * <p>As this Remote Procedure is defined in the ASRC VistA patch, consult the ASRC
     * Technical Manual for parameter and return value documentation.</p>
     */
    SR_ASRC_LAB_RESULTS("SR ASRC LAB RESULTS"),
    
    /**
     * Returns any of the patient's health factors in the last year.
     * 
     * <p>As this Remote Procedure is defined in the ASRC VistA patch, consult the ASRC
     * Technical Manual for parameter and return value documentation.</p>
     */
    SR_ASRC_HEALTH_FACTORS("SR ASRC HEALTH FACTORS"),
    
    /**
     * Returns all active VA and non-VA medications for the patient.
     * 
     * <p>As this Remote Procedure is defined in the ASRC VistA patch, consult the ASRC
     * Technical Manual for parameter and return value documentation.</p>
     */
    SR_ASRC_ACTIVE_MEDS("SR ASRC ACTIVE MEDS"),

    /**
     * Returns all nursing notes with the "NURSING ADMISSION EVALUATION NOTE" title.
     * 
     * <p>As this Remote Procedure is defined in the ASRC VistA patch, consult the ASRC
     * Technical Manual for parameter and return value documentation.</p>
     */
    SR_ASRC_ADL_NOTES("SR ASRC ADL NOTES"),
    
    /**
     * Returns any nursing notes for the specified patient, that contain the specified
     * substring.
     * 
     * <p>As this Remote Procedure is defined in the ASRC VistA patch, consult the ASRC
     * Technical Manual for parameter and return value documentation.</p>
     */
    SR_ASRC_DNR_NOTES("SR ASRC DNR NOTES");

    /**
     * VistA returns this string if {@link #SR_ASRC_SAVE_PROGRESS_NOTE} succeeds.
     */
    public final static String VALID_SIGNATURE_RETURN =
            "1^Progress note was created and signed successfully.";
    
    /**
     * VistA returns this string if {@link #SR_ASRC_RISK_SAVE} succeeds.
     */
    public final static String RISK_SAVED_RETURN =
            "1^Record created successfully.";
    
    /**
     * {@link #XUS_KAAJEE_GET_USER_VIA_PROXY} returns this DUZ if the token was invalid
     * (or associated with a different client IP).
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

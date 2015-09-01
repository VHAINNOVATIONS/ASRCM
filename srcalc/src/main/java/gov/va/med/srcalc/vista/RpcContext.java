package gov.va.med.srcalc.vista;

/**
 * <p>Enumerates all Remote Procedure Call (RPC) contexts which this application uses.</p>
 * 
 * <p>When an application makes a Remote Procedure Call, it specifies an "RPC context",
 * which identifies the application being run. A user must have the associated menu option
 * assigned to them in VistA to execute the RPC.</p>
 * 
 * <p>Most remote procedure calls will be executed under {@link #SR_ASRC}, but sign-on
 * calls will be under {@link #XUS_SIGNON}.</p>
 */
public enum RpcContext
{
    /**
     * For VistA kernel user sign-on.
     */
    XUS_SIGNON("XUS SIGNON"),
    
    XUS_KAAJEE_PROXY_LOGON("XUS KAAJEE PROXY LOGON"),

    /**
     * Identifies the Automated Surgical Risk Calculator application.
     */
    SR_ASRC("SR ASRC");
    
    private final String fContextName;
    
    private RpcContext(final String contextName)
    {
        fContextName = contextName;
    }
    
    /**
     * Returns the name of the RPC context, equal to the VistA menu option name.
     */
    public String getContextName()
    {
        return fContextName;
    }
}

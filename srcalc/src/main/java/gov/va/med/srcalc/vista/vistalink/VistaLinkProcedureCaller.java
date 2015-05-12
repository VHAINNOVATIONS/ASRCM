package gov.va.med.srcalc.vista.vistalink;

import java.util.*;

import javax.naming.*;
import javax.resource.ResourceException;

import gov.va.med.exception.FoundationsException;
import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.vista.RemoteProcedure;
import gov.va.med.srcalc.vista.VistaProcedureCaller;
import gov.va.med.vistalink.adapter.cci.*;
import gov.va.med.vistalink.institution.InstitutionMappingNotFoundException;
import gov.va.med.vistalink.rpc.*;
import gov.va.med.vistalink.security.m.SecurityIdentityDeterminationFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.*;

/**
 * Provides a simple interface to call VistA Remote Procedures. Uses VistALink.
 */
public class VistaLinkProcedureCaller implements VistaProcedureCaller
{
    /**
     * The parameter and result types supported by VistALink. These should
     * really be constants somewhere in VistALink.
     */
    enum VlType
    {
        array,
        string;
    }
    
    /**
     * This is the RPC context for all ASRC RPCs. (This value is determined by
     * VistA.)
     */
    protected static final String RPC_CONTEXT = "SR ASRC";
    
    private static final Logger fLogger = LoggerFactory.getLogger(VistaLinkProcedureCaller.class);
    
    /**
     * The division of the remote VistA.
     */
    private final String fDivision;
    
    /**
     * The JDNI name of the VistaLinkConnectionFactory.
     */
    private final String fVlcfJndiName;
    
    /**
     * The VistaLinkConnectionFactory.
     */
    private final VistaLinkConnectionFactory fVlcf;
    
    /**
     * Constructs an instance for the given VistA division (e.g., 500).
     * @throws ConfigurationException if VistALink is not configured properly
     * @throws IllegalArgumentException if the given division is not known
     */
    public VistaLinkProcedureCaller(final String division)
    {
        fDivision = division;
        
        try
        {
            // TODO: support multiple institutions. I had trouble with the
            // InstitutionMappingDelegate, so I just hardcoded this for now
            if (division.equals("500"))
            {
                fVlcfJndiName = "java:comp/env/vlj/Asrc500";
            }
            else
            {
                throw new InstitutionMappingNotFoundException(
                        "Only station 500 is supported right now.");
            }

            final Context namingContext = new InitialContext();
            fVlcf = (VistaLinkConnectionFactory)namingContext.lookup(
                    fVlcfJndiName);
        }
        catch (InstitutionMappingNotFoundException e)
        {
            throw new IllegalArgumentException("Division " + division + " not supported", e);
        }
        catch (NamingException e)
        {
            throw new ConfigurationException(
                    "Could not load VistaLinkConnectionFactory from JNDI", e);
        }
    }
    
    @Override
    public String getDivision()
    {
        return fDivision;
    }
    
    /**
     * Makes an {@link RpcRequest} object. The primary benefit of this method
     * is to translate the meaningless {@link FoundationsException}.
     * @param procedure the procedure to be called
     * @return the object
     * @throws ConfigurationException if VistALink could not make the object
     */
    protected RpcRequest makeRequestObject(final RemoteProcedure procedure)
    {
        try
        {
            return RpcRequestFactory.getRpcRequest(RPC_CONTEXT, procedure.getProcedureName());
        }
        catch (final FoundationsException ex)
        {
            // No VistA comm occurs here so any Exception is a configuration
            // issue.
            throw new ConfigurationException(
                    "Failed to make VistALink RpcRequest object", ex);
        }
    }
    
    /**
     * Performs the given {@link RpcRequest} under the given DUZ.
     * @param duz the calling user's DUZ
     * @param request the RpcRequest to execute
     * @return an unmodifiable list of String lines from the reponse
     * @throws IllegalArgumentException if the given DUZ is invalid
     * @throws RecoverableDataAccessException if a VistALink connection could
     * not be obtained or any other VistALink error occurs
     */
    protected List<String> doRpc(final String duz, final RpcRequest request)
    {
        fLogger.debug(
                "About to call remote procedure \"{}\"",
                request.getRpcName());

        final VistaLinkDuzConnectionSpec cs =
                new VistaLinkDuzConnectionSpec(fDivision, duz);

        try
        {
            final VistaLinkConnection conn = (VistaLinkConnection)fVlcf.getConnection(cs);
            try
            {
                final RpcResponse response = conn.executeRPC(request);
                fLogger.debug(
                        "Got {} response: {}",
                        response.getResultsType(), response.getResults());
                // The only current possible types are "string" and "array"
                // VistALink represents arrays as newline-delimited strings.
                if(VlType.array.name().equals(response.getResultsType()))
                {
                        // NB: String.split() strips trailing empty strings.
                    return Arrays.asList(response.getResults().split("\n"));
                }
                return Arrays.asList(response.getResults());
            }
            finally
            {
                conn.close();
            }
        }
        catch (SecurityIdentityDeterminationFaultException e)
        {
            throw new IllegalArgumentException("Invalid DUZ", e);
        }
        catch (ResourceException e)
        {
            throw new RecoverableDataAccessException(
                    "Could not obtain connection to VistA", e);
        }
        catch (FoundationsException e)
        {
            throw new RecoverableDataAccessException("VistALink error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> doRpc(
            final String duz, final RemoteProcedure procedure, final String... args)
    {
        final RpcRequest req = makeRequestObject(procedure);
        
        // Set the arguments.
        for (int i = 0; i < args.length; ++i)
        {
            // VistA starts counting at 1, not 0.
            final int vistaParamIndex = i + 1;
            fLogger.debug("Setting parameter {} to {}", vistaParamIndex, args[i]);
            req.getParams().setParam(vistaParamIndex, VlType.string.name(), args[i]);
        }
        
        return doRpc(duz, req);
    }
    
    @Override
    public String doSaveProgressNoteCall(
            final String duz,
            final String encryptedSignature,
            final String patientDfn,
            final List<String> noteLines)
    {
        final RpcRequest req = makeRequestObject(RemoteProcedure.SAVE_PROGRESS_NOTE);
        
        // The DUZ is passed as an explicit parameter here.
        req.getParams().setParam(1, VlType.string.name(), duz);
        req.getParams().setParam(2, VlType.string.name(), encryptedSignature);
        req.getParams().setParam(3, VlType.string.name(), patientDfn);
        
        // The RPC requires an awkward multi-subscript array here. Transform
        // the given List into the expected format.
        final Map<String, String> noteMap = new HashMap<String, String>(noteLines.size());
        for(int i = 0; i < noteLines.size(); i++)
        {
            noteMap.put(
                    // VistA indexing starts at 1
                    RpcRequest.buildMultipleMSubscriptKey(String.format("\"TEXT\",%d,0",i + 1)),
                    noteLines.get(i));
        }
        req.getParams().setParam(4, VlType.array.name(), noteMap);
        
        // We assume only one line in this response.
        return doRpc(duz, req).get(0);
    }
}

package gov.va.med.srcalc.vista.vistalink;

import java.util.*;

import javax.naming.*;
import javax.resource.ResourceException;
import javax.security.auth.login.AccountException;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(VistaLinkProcedureCaller.class);
    
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
            return RpcRequestFactory.getRpcRequest(
                    procedure.getRpcContext().getContextName(),
                    procedure.getProcedureName());
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
     * Sets a string parameter to the given {@link RpcRequestParams}. Provides
     * type-safety and logging (since VistALink provides neither well).
     */
    private void setStringParam(
            final RpcRequestParams params, final int index, final String value)
    {
        LOGGER.debug("Setting param {} to {}", index, value);
        params.setParam(index, VlType.string.name(), value);
    }
    
    /**
     * Sets an array parameter to the given {@link RpcRequestParams}. Provides
     * type-safety and logging (since VistALink provides neither well).
     */
    private void setArrayParam(
            final RpcRequestParams params, final int index, final List<?> value)
    {
        LOGGER.debug("Setting param {} to {}", index, value);
        params.setParam(index, VlType.array.name(), value);
    }
    
    /**
     * Sets an array parameter to the given {@link RpcRequestParams}. Provides
     * type-safety and logging (since VistALink provides neither well).
     */
    private void setArrayParam(
            final RpcRequestParams params, final int index, final Map<String, ?> value)
    {
        LOGGER.debug("Setting param {} to {}", index, value);
        params.setParam(index, VlType.array.name(), value);
    }
    
    /**
     * Performs the given {@link RpcRequest} under the given DUZ.
     * @param duz the calling user's DUZ
     * @param request the RpcRequest to execute
     * @return an unmodifiable list of String lines from the reponse
     * @throws AccountException if VistALink could not find a user with the given DUZ
     * @throws RecoverableDataAccessException if a VistALink connection could
     * not be obtained or any other VistALink error occurs
     */
    protected List<String> doRpc(final String duz, final RpcRequest request)
            throws AccountException
    {
        LOGGER.debug(
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
                LOGGER.debug(
                        "Got {} response: {}",
                        response.getResultsType(), response.getResults());
                // The only current possible types are "string" and "array"
                // VistALink represents arrays as newline-delimited strings.
                if(VlType.array.name().equals(response.getResultsType()))
                {
                    // String.split() is used here instead of Guava's Splitter 
                    // because it eliminates an empty string at the end of the 
                    // response result, which is the desired behavior.
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
            /*
             * Translate the VistALink-specific Exception into a standard Java Exception.
             * 
             * There is an AccountNotFoundException, but I'm not sure that
             * SecurityIdentityDeterminationFaultException is that specific: just use the
             * generic AccountException.
             */
            
            // AccountNotFoundException doesn't accept a cause, so just log it. (The
            // underlying SecurityIdentityDeterminationFaultException doesn't provide much
            // extra information anyway.)
            LOGGER.info("Translating VistALink exception into an AccountException.", e);

            throw new AccountException("Could not find a VistA user for that DUZ.");
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
            throws AccountException
    {
        final RpcRequest req = makeRequestObject(procedure);
        
        // Set the arguments.
        for (int i = 0; i < args.length; ++i)
        {
            // VistA starts counting at 1, not 0.
            final int vistaParamIndex = i + 1;
            setStringParam(req.getParams(), vistaParamIndex, args[i]);
        }
        
        return doRpc(duz, req);
    }
    
    @Override
    public String doSaveProgressNoteCall(
            final String duz,
            final String encryptedSignature,
            final String patientDfn,
            final List<String> noteLines)
            throws AccountException
    {
        final RpcRequest req = makeRequestObject(RemoteProcedure.SAVE_PROGRESS_NOTE);
        
        // The DUZ is passed as an explicit parameter here.
        setStringParam(req.getParams(), 1, duz);
        setStringParam(req.getParams(), 2, encryptedSignature);
        setStringParam(req.getParams(), 3, patientDfn);
        
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
        setArrayParam(req.getParams(), 4, noteMap);
        
        // We assume only one line in this response.
        return doRpc(duz, req).get(0);
    }
    
    @Override
    public String doSaveRiskCalculationCall(
            final String duz,
            final String patientDfn,
            final String cptCode,
            final String dateTime,
            final List<String> outcomes)
            throws AccountException
    {
        final RpcRequest req = makeRequestObject(RemoteProcedure.SAVE_RISK);
        
        setStringParam(req.getParams(), 1, patientDfn);
        setStringParam(req.getParams(), 2, cptCode);
        setStringParam(req.getParams(), 3, dateTime);
        setArrayParam(req.getParams(), 4, outcomes);
        
        // We assume only one line in this response.
        return doRpc(duz, req).get(0);
    }
    
    @Override
    public String doRetrieveLabsCall(
            final String duz,
            final String patientDfn,
            final List<String> labNames)
            throws AccountException
    {
        final RpcRequest req = makeRequestObject(RemoteProcedure.GET_LABS);
        
        setStringParam(req.getParams(), 1, patientDfn);
        setArrayParam(req.getParams(), 2, labNames);
        
        // We assume only one line in this response.
        return doRpc(duz, req).get(0);
    }
}

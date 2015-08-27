package gov.va.med.srcalc.vista.vistalink;

import java.util.*;

import javax.naming.*;
import javax.resource.ResourceException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import gov.va.med.exception.FoundationsException;
import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.vista.RemoteProcedure;
import gov.va.med.srcalc.vista.VistaProcedureCaller;
import gov.va.med.vistalink.adapter.cci.*;
import gov.va.med.vistalink.institution.InstitutionMapNotInitializedException;
import gov.va.med.vistalink.institution.InstitutionMappingDelegate;
import gov.va.med.vistalink.institution.InstitutionMappingNotFoundException;
import gov.va.med.vistalink.rpc.*;
import gov.va.med.vistalink.security.m.SecurityAccessVerifyCodePairInvalidException;
import gov.va.med.vistalink.security.m.SecurityFaultException;
import gov.va.med.vistalink.security.m.SecurityIdentityDeterminationFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.*;

/**
 * <p>Provides a simple interface to call VistA Remote Procedures. Uses VistALink.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class VistaLinkProcedureCaller implements VistaProcedureCaller
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
     * @throws IllegalArgumentException if the given division is not known. (Call
     * {@link VistaLinkUtil#isDivisionKnown(String)} first if unsure.)
     */
    public VistaLinkProcedureCaller(final String division)
    {
        fDivision = division;
        
        try
        {
            fVlcfJndiName = InstitutionMappingDelegate.getJndiConnectorNameForInstitution(
                    division);
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
        catch (InstitutionMapNotInitializedException e)
        {
            throw new ConfigurationException(
                    "VistALink institution map not loaded.", e);
        }
    }
    
    @Override
    public String getDivision()
    {
        return fDivision;
    }

    @Override
    public List<String> doRpc(
            final String duz, final RemoteProcedure procedure, final String... args)
            throws LoginException
    {
        final RpcRequest req = makeRequestObject(procedure, args);
        
        return doRpc(duz, req);
    }
    
    /**
     * <p>Like {@link #doRpc(String, RemoteProcedure, String...)}, but supports any
     * VistaLinkConnectionSpec. Intended to facilitate authentication via different
     * methods.</p>
     * 
     * <p>Note that the thrown Exceptions are more specific than for other methods to
     * facilitate authentication failure reporting.</p>
     * 
     * @throws FailedLoginException if the give ConnectionSpec specified an access/verify
     * code pair but it was not correct
     * @throws AccountNotFoundException if the given ConnectionSpec specified a user
     * identifier (e.g., DUZ) that could not be matched to a VistA user
     * @throws LoginException if unable to re-authenticate using the given ConnectionSpec
     * for any other reason
     * @throws DataAccessException if an error occurs in communicating with VistA
     */
    public List<String> doRpc(
            final VistaLinkConnectionSpec connectionSpec,
            final RemoteProcedure procedure,
            final String... args)
            throws DataAccessException, FailedLoginException, AccountNotFoundException,
                    LoginException
    {
        return doRpc(connectionSpec, makeRequestObject(procedure, args));
    }
    
    @Override
    public String doSaveProgressNoteCall(
            final String duz,
            final String encryptedSignature,
            final String patientDfn,
            final List<String> noteLines)
            throws LoginException
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
            throws LoginException
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
            throws LoginException
    {
        final RpcRequest req = makeRequestObject(RemoteProcedure.GET_LABS);
        
        setStringParam(req.getParams(), 1, patientDfn);
        setArrayParam(req.getParams(), 2, labNames);
        
        // We assume only one line in this response.
        return doRpc(duz, req).get(0);
    }
    
    /**
     * Makes an {@link RpcRequest} object. The primary benefit of this method
     * is to translate the meaningless {@link FoundationsException}.
     * @param procedure the procedure to be called
     * @return the object
     * @throws ConfigurationException if VistALink could not make the object
     */
    private RpcRequest makeRequestObject(final RemoteProcedure procedure)
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
     * Makes an {@link RpcRequest} object for the given procedure with the given
     * arguments.
     * @throws ConfigurationException if VistALink could not make the object
     */
    private RpcRequest makeRequestObject(final RemoteProcedure procedure, final String... args)
    {
        final RpcRequest req = makeRequestObject(procedure);
        
        // Set the arguments.
        for (int i = 0; i < args.length; ++i)
        {
            // VistA starts counting at 1, not 0.
            final int vistaParamIndex = i + 1;
            setStringParam(req.getParams(), vistaParamIndex, args[i]);
        }
        return req;
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
     * @throws AccountNotFoundException if VistALink could not find a user with the given
     * DUZ
     * @throws LoginException if any other issue occurred reauthenticating in VistA
     * @throws RecoverableDataAccessException if a VistALink connection could
     * not be obtained or any other VistALink error occurs
     */
    private List<String> doRpc(final String duz, final RpcRequest request)
            throws AccountNotFoundException, LoginException
    {
        LOGGER.debug(
                "About to call remote procedure \"{}\"",
                request.getRpcName());

        final VistaLinkDuzConnectionSpec cs =
                new VistaLinkDuzConnectionSpec(fDivision, duz);

        try
        {
            return doRpc(cs, request);
        }
        catch (final AccountNotFoundException e)
        {
            // We know we were using DUZ: be specific.
            throw new AccountNotFoundException(
                    "Could not find a VistA user with that DUZ.");
        }
    }

    /**
     * Makes the specified remote procedure call. Translates all VistALink exceptions to
     * either standard Java or Spring exceptions.
     * @param connectionSpec specifies connection parameters (e.g., division, user)
     * @param request specifies the remote procedure call to make
     * @return an unmodifiable list of String lines from the reponse
     * @throws DataAccessException if there was an error communicating with VistA
     * @throws AccountNotFoundException if the given ConnectionSpec specified a user
     * identifier (e.g., DUZ) that could not be matched to a VistA user
     * @throws FailedLoginException if the given ConnectionSpec specified an access/verify
     * code pair but it was not correct
     * @throws LoginException if any other issue occurred reauthenticating in VistA
     */
    private List<String> doRpc(
            final VistaLinkConnectionSpec connectionSpec, final RpcRequest request)
            throws DataAccessException, FailedLoginException, AccountNotFoundException,
                    LoginException
    {
        try
        {
            final VistaLinkConnection conn = (VistaLinkConnection)fVlcf.getConnection(connectionSpec);
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
        catch (final ResourceException e)
        {
            // Per the docs, ResourceException can be thrown for anything from a 
            // configuration issue to a failure to allocate resources.
            // RecoverableDataAccessException is our best guess here.
            throw new RecoverableDataAccessException(
                    "Could not obtain connection to VistA", e);
        }
        // VistALink only specifies VistaLinkFaultException, but these exceptions have
        // been determined empirically.
        catch (final SecurityAccessVerifyCodePairInvalidException e)
        {
            LOGGER.debug(
                    "Translating SecurityAccessVerifyCodePairInvalidException into FailedLoginException.",
                    e);
            
            throw new FailedLoginException("Bad access or verify code.");
        }
        catch (final SecurityIdentityDeterminationFaultException e)
        {
            // AccountNotFoundException doesn't accept a cause, so just log it. (The
            // underlying SecurityIdentityDeterminationFaultException doesn't provide much
            // extra information anyway.)
            LOGGER.debug(
                    "Translating VistALink exception into an AccountNotFoundException.",
                    e);

            throw new AccountNotFoundException(
                    // We don't know what identifier type (DUZ, etc.) was actually used
                    // in the ConnectionSpec, so be generic.
                    "Could not find a VistA user with that identifier.");
        }
        catch (final SecurityFaultException e)
        {
            // LoginException doesn't accept a cause, so just log it. Log this one at INFO
            // level because we don't expect many of these.
            LOGGER.info("Translating VistALink exception into a LoginException.", e);

            throw new LoginException("Unable to re-authenticate: " + e.getMessage());
        }
        // Catch-all: something went wrong in VistALink but we don't know what.
        catch (final FoundationsException e)
        {
            throw new RecoverableDataAccessException(
                    "VistALink error: " + e.getMessage(), e);
        }
    }
}

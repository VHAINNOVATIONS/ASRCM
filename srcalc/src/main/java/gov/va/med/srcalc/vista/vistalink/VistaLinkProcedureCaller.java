package gov.va.med.srcalc.vista.vistalink;

import java.util.Arrays;
import java.util.List;

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
     * The String identifying the VistaLink result is an array. (This should
     * really be a constant in {@link RpcResponse}.)
     */
    public static final String RESULT_TYPE_ARRAY = "array";
    
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

    @Override
    public List<String> doRpc(final String duz, final RemoteProcedure procedure, final String... args)
    {
        // This is the RPC context for all ASRC RPCs. (This value is determined
        // by VistA.)
        final String rpcContext = "SR ASRC";

        final VistaLinkDuzConnectionSpec cs =
                new VistaLinkDuzConnectionSpec(fDivision, duz);

        try
        {
            fLogger.debug("About to call remote procedure \"{}\"", procedure.getProcedureName());
            final RpcRequest req = RpcRequestFactory.getRpcRequest(
                    rpcContext, procedure.getProcedureName());
            
            // Set the arguments.
            for (int i = 0; i < args.length; ++i)
            {
                // VistA starts counting at 1, not 0.
                final int vistaParamIndex = i + 1;
                final String arg = args[i];
                fLogger.debug("Setting parameter {} to {}", vistaParamIndex, arg);
                req.getParams().setParam(vistaParamIndex, "string", arg);
            }

            final VistaLinkConnection conn = (VistaLinkConnection)fVlcf.getConnection(cs);
            try
            {
                final RpcResponse response = conn.executeRPC(req);
                fLogger.debug(
                        "Got {} response: {}",
                        response.getResultsType(), response.getResults());
                // The only current possible types are "string" and "array"
                // VistALink represents arrays as newline-delimited strings.
                if(response.getResultsType().equals("array"))
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
        catch (FoundationsException e)
        {
            throw new RecoverableDataAccessException(
                    "An internal VistaLink error occurred", e);
        }
        catch (ResourceException e)
        {
            throw new RecoverableDataAccessException(
                    "Could not obtain connection to VistA", e);
        }
    }
    
}

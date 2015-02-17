package gov.va.med.srcalc.vista;

import javax.naming.*;
import javax.resource.ResourceException;

import gov.va.med.exception.FoundationsException;
import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.vistalink.adapter.cci.*;
import gov.va.med.vistalink.institution.InstitutionMappingNotFoundException;
import gov.va.med.vistalink.rpc.*;
import gov.va.med.vistalink.security.m.SecurityIdentityDeterminationFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.RecoverableDataAccessException;

/**
 * Provides a simple interface to call VistA Remote Procedures.
 */
public class VistaProcedureCaller
{
    private static final Logger fLogger = LoggerFactory.getLogger(VistaProcedureCaller.class);
    
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
    public VistaProcedureCaller(final String division)
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
    
    public String getDivision()
    {
        return fDivision;
    }

    /**
     * Performs a Remote Procedure Call.
     * @param duz the calling user's DUZ
     * @param rpcName the name of the remote procedure
     * @param args the remote procedure arguments, if any
     * @return the RpcResponse object
     * @throws IllegalArgumentException if the provided DUZ is invalid
     * @throws RecoverableDataAccessException if a VistALink error occurred
     */
    protected RpcResponse doRpc(final String duz, final String rpcName, final String... args)
    {
        final String rpcContext = "SR ASRC";

        final VistaLinkDuzConnectionSpec cs =
                new VistaLinkDuzConnectionSpec(fDivision, duz);

        try
        {
            fLogger.debug("About to call remote procedure \"{}\"", rpcName);
            final RpcRequest req = RpcRequestFactory.getRpcRequest(
                    rpcContext, rpcName);
            
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
                return conn.executeRPC(req);
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

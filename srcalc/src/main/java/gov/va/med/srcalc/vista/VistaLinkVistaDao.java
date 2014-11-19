package gov.va.med.srcalc.vista;

import javax.naming.*;
import javax.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.RecoverableDataAccessException;

import gov.va.med.exception.FoundationsException;
import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.vistalink.adapter.cci.*;
import gov.va.med.vistalink.institution.*;
import gov.va.med.vistalink.rpc.*;
import gov.va.med.vistalink.security.m.SecurityIdentityDeterminationFaultException;

/**
 * Reads and writes remote VistA data.
 */
public class VistaLinkVistaDao implements VistaDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(VistaLinkVistaDao.class);

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
    public VistaLinkVistaDao(final String division)
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
    
    public VistaPerson loadVistaPerson(final String duz)
    {
        fLogger.debug("Loading VistaPerson for duz {}.", duz);

        final String rpcContext = "XOBV VISTALINK TESTER";

        final VistaLinkDuzConnectionSpec cs =
                new VistaLinkDuzConnectionSpec(fDivision, duz);
        
        try
        {
            final RpcRequest req = RpcRequestFactory.getRpcRequest(
                    rpcContext, "SR ASRC USER");
            final VistaLinkConnection conn = (VistaLinkConnection)fVlcf.getConnection(cs);
            try
            {
                final RpcResponse response = conn.executeRPC(req);
                
                // Get the first index in the array. We don't care about the rest.
                final String userString = response.getResults().split("\n")[0];
                fLogger.debug("Got user: ", userString);
                return new VistaPerson(duz, userString, "user class not pulled");
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

package gov.va.med.srcalc.vista.vistalink;

import java.util.List;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.vista.RemoteProcedure;
import gov.va.med.srcalc.vista.VistaAuthenticator;
import gov.va.med.vistalink.adapter.cci.VistaLinkAppProxyConnectionSpec;

/**
 * <p>A VistaAuthenticator that uses VistALink to perform VistA operations.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class VistaLinkAuthenticator implements VistaAuthenticator
{
    /**
     * The name of the application proxy user to use for authentication RPCs.
     */
    static final String APPLICATON_PROXY_USER = "KAAJEE,PROXY";

    private static final Logger LOGGER =
            LoggerFactory.getLogger(VistaLinkAuthenticator.class);
    
    private final VistaLinkProcedureCaller fProcedureCaller;
    
    /**
     * Constructs an instance that will authenticate with the specified VistA division.
     * @throws IllegalArgumentException if the given division is not known to VistALink
     */
    public VistaLinkAuthenticator(final String division)
    {
        fProcedureCaller = new VistaLinkProcedureCaller(division);
    }
    
    @Override
    public String getDivision()
    {
        return fProcedureCaller.getDivision();
    }
    
    @Override
    public VistaPerson authenticateViaAccessVerify(
            final String accessCode, final String verifyCode, final String clientIp)
            throws FailedLoginException, LoginException, DataAccessException
    {
        LOGGER.debug("Authenticating access/verify code from {}", clientIp);

        final AccessVerifyConnectionSpec connectionSpec = new AccessVerifyConnectionSpec(
                getDivision(), accessCode, verifyCode, clientIp);
        final List<String> userResults = fProcedureCaller.doRpc(
                connectionSpec, RemoteProcedure.XUS_GET_USER_INFO);
        
        // Just get the pieces we care about.
        final String duz = userResults.get(0);
        final String userName = userResults.get(1);
        final VistaPerson person = new VistaPerson(
                fProcedureCaller.getDivision(),
                duz,
                userName,
                loadProviderType(duz));
        LOGGER.debug("Successfully authenticated VistA user: {}", person);
        return person;
    }
    
    @Override
    public VistaPerson authenticateViaCcowToken(
            final String ccowToken, final String clientIp)
            throws FailedLoginException, LoginException, DataAccessException
    {
        LOGGER.debug("Authenticating CCOW token from {}", clientIp);
        
        final VistaLinkAppProxyConnectionSpec connectionSpec =
                new VistaLinkAppProxyConnectionSpec(getDivision(), APPLICATON_PROXY_USER);

        final List<String> userResults;
        try
        {
            userResults = fProcedureCaller.doRpc(
                    connectionSpec,
                    RemoteProcedure.XUS_KAAJEE_GET_USER_VIA_PROXY,
                    clientIp,
                    // FIXME: reference this from elsewhere
                    "Automated Surgical Risk Calculator",
                    VistaLinkUtil.encrypt("~~TOK~~" + ccowToken));
        }
        catch (final LoginException e)
        {
            // If we get a LoginException here, it means our application proxy user is not
            // working.
            throw new InvalidDataAccessResourceUsageException(
                    "Unable to call Remote Procedure as application proxy.", e);
        }

        // Just get the pieces we care about.
        final String duz = userResults.get(0);
        // The RPC doesn't fail if the CCOW token was invalid, it just returns a DUZ of
        // 0.
        if (duz.equals(RemoteProcedure.BAD_TOKEN_DUZ))
        {
            throw new FailedLoginException("VistA rejected the CCOW token");
        }
        final String userName = userResults.get(1);
        final VistaPerson person = new VistaPerson(
                fProcedureCaller.getDivision(),
                duz,
                userName,
                loadProviderType(duz));
        LOGGER.debug("Successfully authenticated VistA user via CCOW: {}", person);
        return person;
    }
    
    /**
     * Loads the user's single Provider Type, if present.
     */
    private Optional<String> loadProviderType(final String duz) throws LoginException
    {
        final List<String> personClassResults = fProcedureCaller.doRpc(
                duz, RemoteProcedure.SR_ASRC_PERSON_CLASSES);
        
        if (!personClassResults.isEmpty())
        {
            // The RPC returns an array, but a user should only have one non-expired
            // Person Class in VistA.
            if (personClassResults.size() > 1)
            {
                LOGGER.warn(
                        "VistA unexpectedly returned more than one person class for " +
                        " {}@{}: {}. All but the first one will be discarded.",
                        duz,
                        fProcedureCaller.getDivision(),
                        personClassResults);
            }
            
            return Optional.of(personClassResults.get(0));
        }
        else
        {
            return Optional.absent();
        }
    }
}

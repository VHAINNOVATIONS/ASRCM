package gov.va.med.srcalc.vista.vistalink;

import java.util.List;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.vista.RemoteProcedure;

/**
 * Authenticates VistA credentials, providing a {@link VistaPerson} if successful.
 */
public class VistaLinkAuthenticator
{
    private static final Logger LOGGER =
            LoggerFactory.getLogger(VistaLinkAuthenticator.class);
    
    private final VistaLinkProcedureCaller fProcedureCaller;
    
    /**
     * Constructs an instance what will authenticate with the specified VistA division.
     * @throws IllegalArgumentException if the given division is not known to VistALink
     */
    public VistaLinkAuthenticator(final String division)
    {
        fProcedureCaller = new VistaLinkProcedureCaller(division);
    }
    
    /**
     * Loads the user's single Provider Type, if present.
     */
    private Optional<String> loadProviderType(final String duz) throws LoginException
    {
        final List<String> personClassResults = fProcedureCaller.doRpc(
                duz, RemoteProcedure.GET_USER_PERSON_CLASSES);
        
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
    
    /**
     * Authenticates a user given an access/verify code pair.
     * @param accessCode the user's plain-text access code
     * @param verifyCode the user's plain-text verify code
     * @param clientIp the requesting client's IP address
     * @return a VistaPerson object for the user if authentication was successful
     * @throws FailedLoginException if the access/verify pair was incorrect
     * @throws LoginException if VistA rejected the authentication for any other reason
     * (e.g., not authorized for the division, user disabled, etc.)
     * @throws DataAccessException if communication with VistA fails
     */
    public VistaPerson authenticateViaAccessVerify(
            final String accessCode, final String verifyCode, final String clientIp)
            throws FailedLoginException, LoginException, DataAccessException
    {
        LOGGER.debug("Authenticating access/verify code from {}", clientIp);

        final AccessVerifyConnectionSpec connectionSpec = new AccessVerifyConnectionSpec(
                fProcedureCaller.getDivision(), accessCode, verifyCode, clientIp);
        final List<String> userResults = fProcedureCaller.doRpc(
                connectionSpec, RemoteProcedure.GET_USER_INFO);
        
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
    
}

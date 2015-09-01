package gov.va.med.srcalc.vista;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.springframework.dao.DataAccessException;

import gov.va.med.srcalc.domain.VistaPerson;

/**
 * <p>Authenticates VistA credentials, providing a {@link VistaPerson} if successful.</p>
 * 
 * <p>This is an interface to allow for easy mocking.</p>
 */
public interface VistaAuthenticator
{
    /**
     * Returns the VistA division with which this object authenticates users.
     */
    public String getDivision();
    
    /**
     * Authenticates a user given an access/verify code pair.
     * @param accessCode the user's plain-text access code
     * @param verifyCode the user's plain-text verify code
     * @param clientIp the requesting client's IP address
     * @return a VistaPerson object for the user if authentication was successful
     * @throws FailedLoginException if the access/verify pair was incorrect
     * @throws LoginException if VistA rejected the authentication for any other reason
     * (e.g., not authorized for the division, user disabled, etc.)
     * @throws DataAccessException if communication with VistA failed
     */
    public VistaPerson authenticateViaAccessVerify(
            final String accessCode, final String verifyCode, final String clientIp)
            throws FailedLoginException, LoginException, DataAccessException;

    /**
     * <p>Authenticates a user given a CCOW token.</p>
     * 
     * <p>CPRS, and presumably other VA CCOW-enabled applications, do not populate the
     * CCOW context with the user's DUZ or any other explicit user identifier. They
     * instead populate the context with an opaque "CCOW token" representing the specific
     * login. (I.e., VistA assigns a new token per login.) Applications can use this
     * method to authenticate with a CCOW token and load the user's information.</p>
     * 
     * @param ccowToken the CCOW token
     * @param clientIp the requesting client's IP address
     * @return a VistaPerson object for the user if authentication was successful
     * @throws FailedLoginException if VistA rejects the CCOW token
     * @throws LoginException if VistA rejects the login for any other reason
     * @throws DataAccessException if communication with VistA failed
     * @see RemoteProcedure#GET_USER_FROM_CCOW
     */
    public VistaPerson authenticateViaCcowToken(
            final String ccowToken, final String clientIp)
            throws FailedLoginException, LoginException, DataAccessException;
}

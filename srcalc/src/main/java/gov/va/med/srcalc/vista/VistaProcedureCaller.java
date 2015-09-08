package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.ConfigurationException;

import java.util.List;

import javax.security.auth.login.LoginException;

import org.springframework.dao.DataAccessException;

/**
 * An object that can call VistA Remote Procedures.
 */
public interface VistaProcedureCaller
{
    /**
     * Performs a generic Remote Procedure Call with all-string parameters.
     * @param duz the calling user's DUZ
     * @param procedure the remote procedure to call
     * @param args the remote procedure arguments, if any
     * @return an unmodifiable list of String lines from the response
     * @throws LoginException if unable to authenticate with VistA
     * @throws ConfigurationException if some misconfiguration prevents communication
     * @throws DataAccessException if some error occurred communicating with VistA
     */
    public List<String> doRpc(
            final String duz, final RemoteProcedure procedure, final String... args)
            throws LoginException, DataAccessException;
    
    /**
     * A special-purpose method to call {@link RemoteProcedure#SR_ASRC_SAVE_PROGRESS_NOTE}
     * because its parameters are complex.
     * @param duz the calling user's DUZ (also the signer)
     * @param encryptedSignature the user's electronic signature code
     * @param patientDfn the associated patient's DFN
     * @param noteLines a List of note body lines
     * @return the VistA response as a String (See {@link RemoteProcedure#VALID_SIGNATURE_RETURN}.)
     * @throws LoginException if unable to authenticate as the calling user with VistA
     * @throws ConfigurationException if some misconfiguration prevents communication
     * @throws DataAccessException if some error occurred communicating with VistA
     */
    public String doSaveProgressNoteCall(
            final String duz,
            final String encryptedSignature,
            final String patientDfn,
            final List<String> noteLines)
            throws LoginException, DataAccessException;
    
    /**
     * A special-purpose method to call {@link RemoteProcedure#SR_ASRC_RISK_SAVE}
     * because it requires a List parameter.
     * @param duz the calling user's DUZ
     * @param patientDfn the associated patient's DFN
     * @param cptCode the CPT code to store (optional, may be an empty string)
     * @param dateTime the signature date/time in MM/DD/YYYY@HHMM format
     * @param outcomes the list of outcomes, each outcoming as a String "Outcome name^XX.X"
     * @return the VistA response as a String. (See {@link RemoteProcedure#RISK_SAVED_RETURN}.)
     * @throws LoginException if no VistA user could be matched to the given DUZ
     * @throws DataAccessException if some error occurred communicating with VistA
     */
    public String doSaveRiskCalculationCall(
            final String duz,
            final String patientDfn,
            final String cptCode,
            final String dateTime,
            final List<String> outcomes)
            throws LoginException, DataAccessException;
    
    /**
     * A special-purpose method to call {@link RemoteProcedure#SR_ASRC_LAB_RESULTS}
     * because it requires a List parameter.
     * @param duz the calling user's DUZ
     * @param patientDfn the associated patient's DFN
     * @param labNames the list of potential lab names to retrieve results for
     * @return the VistA response as a String.
     * @throws LoginException if no VistA user could be matched to the given DUZ
     * @throws DataAccessException if some error occurred communicating with VistA
     */
    public String doRetrieveLabsCall(
            final String duz,
            final String patientDfn,
            final List<String> labNames)
            throws LoginException, DataAccessException;
    
    /**
     * Returns the division identifier (including any suffix) for the target
     * VistA.
     */
    public String getDivision();
}

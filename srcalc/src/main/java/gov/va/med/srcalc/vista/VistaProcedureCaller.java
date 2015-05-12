package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.ConfigurationException;

import java.util.List;

public interface VistaProcedureCaller
{
    /**
     * Performs a generic Remote Procedure Call with all-string parameters.
     * @param duz the calling user's DUZ
     * @param procedure the remote procedure to call
     * @param args the remote procedure arguments, if any
     * @return an unmodifiable list of String lines from the response
     * @throws IllegalArgumentException if the provided DUZ is invalid
     * @throws ConfigurationException if some misconfiguration prevents communication
     * @throws DataAccessException if some error occurred communicating with
     * VistA
     */
    public List<String> doRpc(
            final String duz, final RemoteProcedure procedure, final String... args);
    
    /**
     * A special-purpose method to call {@link RemoteProcedure#SAVE_PROGRESS_NOTE}
     * because its parameters are complex.
     * @param duz the calling user's DUZ (also the signer)
     * @param encryptedSignature the user's electronic signature code
     * @param patientDfn the associated patient's DFN
     * @param noteLines a List of note body lines
     * @return the VistA response as a String (See {@link RemoteProcedure#VALID_SIGNATURE_RETURN}.)
     * @throws IllegalArgumentException if the provided DUZ is invalid
     * @throws ConfigurationException if some misconfiguration prevents communication
     * @throws DataAccessException if some error occurred communicating with
     * VistA
     */
    public String doSaveProgressNoteCall(
            final String duz,
            final String encryptedSignature,
            final String patientDfn,
            final List<String> noteLines);
    
    /**
     * Returns the division identifier (including any suffix) for the target
     * VistA.
     */
    public String getDivision();
}

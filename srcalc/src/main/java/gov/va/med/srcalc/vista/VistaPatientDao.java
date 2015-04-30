package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.domain.Patient;

/**
 * <p>Data Access Object for VistA Patients.</p>
 * 
 * <p>This is an interface to allow for easy mocking. See {@link
 * RpcVistaPatientDao} for the "real" implementation.</p>
 */
public interface VistaPatientDao
{
	/**
	 * The maximum amount of characters allowed on a line before wrapping the line
	 * onto a new line.
	 */
	public static final int MAX_LINE_LENGTH = 80;
	
    /**
     * Loads a Patient from VistA given his/her DFN.
     * @throws DataAccessException if communication with VistA failed
     */
    public Patient getPatient(final int dfn);
    
    /**
     * Saves the finished calculation to VistA, given the calculation, electronic signature, 
     * and the note body. Each line of the noteBody is wrapped at {@link VistaPatientDao#MAX_LINE_LENGTH}
     * characters so that the note is easily visible in CPRS without horizontal scrolling.
     * @param patient
     * @param noteBody 
     * @return
     */
    public String saveRiskCalculationNote(final Patient patient, final String electronicSignature, String noteBody);
    
}

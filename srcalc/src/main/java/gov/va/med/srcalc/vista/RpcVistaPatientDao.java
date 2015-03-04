package gov.va.med.srcalc.vista;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.NonTransientDataAccessResourceException;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.util.VistaFieldTranslation;

/**
 * Implementation of {@link VistaPatientDao} using remote procedures.
 */
public class RpcVistaPatientDao implements VistaPatientDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(RpcVistaPatientDao.class);
    
    private final VistaProcedureCaller fProcedureCaller;
    
    private final String fDuz;
    
    /**
     * Constructs an instance.
     * @param procedureCaller for making the procedure calls
     * @param duz the user DUZ under which to perform the procedure calls
     */
    public RpcVistaPatientDao(
            final VistaProcedureCaller procedureCaller, final String duz)
    {
        fProcedureCaller = procedureCaller;
        fDuz = duz;
    }
    
    @Override
    public Patient getPatient(final int dfn)
    {
        final List<String> results;
        	results = fProcedureCaller.doRpc(
                fDuz, RemoteProcedure.GET_PATIENT, Integer.toString(dfn));
    	try
    	{
	        // Fields are separated by '^'
	        final String[] fieldArray = results.get(0).split("\\^");
	        final String patientName = fieldArray[0];
	        final int patientAge = Integer.parseInt(fieldArray[1]);
	        final String patientGender = VistaFieldTranslation.TRANSLATION_MAP.get(fieldArray[2]);
	        final Patient patient = new Patient(dfn, patientName, patientGender, patientAge);
	        fLogger.debug("Loaded {} from VistA.", patient);
	        return patient;
    	}
    	catch(final Exception e)
    	{
    		// There are many DataAccessExcpeionts, but this seems like 
    		// the most appropriate exception to throw here.
    		throw new NonTransientDataAccessResourceException(e.getMessage());
    	}
    }
    
}

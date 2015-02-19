package gov.va.med.srcalc.vista;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.med.srcalc.domain.Patient;

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
        final List<String> results = fProcedureCaller.doRpc(
                fDuz, RemoteProcedure.GET_PATIENT, Integer.toString(dfn));
        
        final String patientName = results.get(0).split("\\^")[0];
        final Patient patient = new Patient(dfn, patientName);
        fLogger.debug("Loaded {} from VistA.", patient);
        return patient;
    }
    
}

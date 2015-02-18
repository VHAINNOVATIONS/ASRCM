package gov.va.med.srcalc.vista;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.med.srcalc.domain.Patient;
import gov.va.med.vistalink.rpc.RpcResponse;

public class VistaLinkVistaPatientDao implements VistaPatientDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(VistaLinkVistaPatientDao.class);
    
    private final VistaProcedureCaller fProcedureCaller;
    
    private final String fDuz;
    
    /**
     * Constructs an instance.
     * @param division the division of the VistA to connect to
     * @param duz the user DUZ under which to perform the procedure calls
     */
    public VistaLinkVistaPatientDao(final String division, final String duz)
    {
        fProcedureCaller = new VistaProcedureCaller(division);
        fDuz = duz;
    }
    
    @Override
    public Patient getPatient(final int dfn)
    {
        final RpcResponse response = fProcedureCaller.doRpc(
                fDuz, "SR ASRC PATIENT", Integer.toString(dfn));
        
        final String patientName = response.getResults().split("\\^")[0];
        final Patient patient = new Patient(dfn, patientName);
        fLogger.debug("Loaded {} from VistA.", patient);
        return patient;
    }
    
}

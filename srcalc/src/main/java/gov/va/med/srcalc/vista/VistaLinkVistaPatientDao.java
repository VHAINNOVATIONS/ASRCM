package gov.va.med.srcalc.vista;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.med.srcalc.domain.Patient;

public class VistaLinkVistaPatientDao implements VistaPatientDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(VistaLinkVistaPatientDao.class);
    
    private final VistaLinkProcedureCaller fProcedureCaller;
    
    private final String fDuz;
    
    /**
     * Constructs an instance.
     * @param division the division of the VistA to connect to
     * @param duz the user DUZ under which to perform the procedure calls
     */
    public VistaLinkVistaPatientDao(final String division, final String duz)
    {
        fProcedureCaller = new VistaLinkProcedureCaller(division);
        fDuz = duz;
    }
    
    @Override
    public Patient getPatient(final int dfn)
    {
        final List<String> results = fProcedureCaller.doRpc(
                fDuz, "SR ASRC PATIENT", Integer.toString(dfn));
        
        final String patientName = results.get(0).split("\\^")[0];
        final Patient patient = new Patient(dfn, patientName);
        fLogger.debug("Loaded {} from VistA.", patient);
        return patient;
    }
    
}

package gov.va.med.srcalc.vista;

import java.util.ArrayList;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import gov.va.med.srcalc.domain.calculation.SignedResult;

/**
 * Implementation of {@link VistaSurgeryDao} using remote procedures. Each
 * instance is tied to a particular user to avoid having to specify the user
 * when calling each method.
 */
public class RpcVistaSurgeryDao implements VistaSurgeryDao
{
    private static final DateTimeFormatter VISTA_DATE_TIME_FORMAT = 
            DateTimeFormat.forPattern("MM/dd/YYYY@HHmm");

    private final VistaProcedureCaller fProcedureCaller;
    private final String fDuz;
    
    public RpcVistaSurgeryDao(
            final VistaProcedureCaller procedureCaller, final String duz)
    {
        fProcedureCaller = procedureCaller;
        fDuz = duz;
    }
    
    @Override
    public void saveCalculationResult(final SignedResult result)
    {
        // The VistaOperationResult that we expect.
        final VistaOperationResult expectedResult =
                VistaOperationResult.fromString(RemoteProcedure.RISK_SAVED_RETURN);

        // Translate a missing CPT to an empty string per specification.
        final String cptString = result.getCptCode().or("");
        
        // Translate the outcomes to Strings per specification.
        final ArrayList<String> outcomes = new ArrayList<>(result.getOutcomes().size());
        for (final Map.Entry<String, Float> entry : result.getOutcomes().entrySet())
        {
            outcomes.add(String.format(
                    // Minimum width 4 characters: "XX.X"
                    "%s^%04.1f", entry.getKey(), entry.getValue() * 100));
        }

        final String rpcResultString = fProcedureCaller.doSaveRiskCalculationCall(
                fDuz,
                String.valueOf(result.getPatientDfn()),
                cptString,
                VISTA_DATE_TIME_FORMAT.print(result.getSignatureTimestamp()),
                outcomes);
        
        final VistaOperationResult rpcResult =
                VistaOperationResult.fromString(rpcResultString);
        // Just compare the codes.
        if (!expectedResult.getCode().equals(rpcResult.getCode()))
        {
            // Any error return now is not due to a network failure, we provided
            // bad data. Throw a non-transient exception.
            throw new InvalidDataAccessResourceUsageException(rpcResult.getMessage());
        }
    }
}

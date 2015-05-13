package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.domain.calculation.SignedResult;

/**
 * <p>Data Access Object for VistA Surgery package objects, particularly risk
 * calculations.</p>
 * 
 * <p>This is an interface to allow for easy mocking. See {@link
 * RpcVistaSurgeryDao} for the "real" implementation.</p>
 */
public interface VistaSurgeryDao
{
    public void saveCalculationResult(final SignedResult result);
}

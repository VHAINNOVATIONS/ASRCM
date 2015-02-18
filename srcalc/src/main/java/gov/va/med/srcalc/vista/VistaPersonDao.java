package gov.va.med.srcalc.vista;

import org.springframework.dao.DataAccessException;

import gov.va.med.srcalc.domain.VistaPerson;

/**
 * <p>Data Access Object for VistA Persons (i.e., users).</p>
 * 
 * <p>This is an interface to allow for easy mocking. See {@link
 * RpcVistaPersonDao} for the "real" implementation.</p>
 */
public interface VistaPersonDao
{
    /**
     * Loads a VistaPerson from VistA given his/her DUZ.
     * @throws DataAccessException if communication with VistA failed
     * @throws IllegalArgumentException if the DUZ was not a valid DUZ
     */
    public VistaPerson loadVistaPerson(final String duz);
}

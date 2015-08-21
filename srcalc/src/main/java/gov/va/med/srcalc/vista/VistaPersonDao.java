package gov.va.med.srcalc.vista;

import javax.security.auth.login.AccountException;

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
     * @throws AccountException if no VistA user could be matched to the given DUZ
     */
    public VistaPerson loadVistaPerson(final String duz) throws AccountException;
}

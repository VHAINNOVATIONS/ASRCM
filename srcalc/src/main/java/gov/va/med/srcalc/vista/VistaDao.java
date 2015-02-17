package gov.va.med.srcalc.vista;

import org.springframework.dao.DataAccessException;

import gov.va.med.srcalc.domain.VistaPerson;

/**
 * <p>Reads and writes remote VistA data.</p>
 * 
 * <p>This is an interface to allow for
 * <a href="http://martinfowler.com/eaaCatalog/serviceStub.html">Service Stubs
 * </a>. See {@link VistaLinkVistaDao} for the "real" implementation.</p>
 */
public interface VistaDao
{
    /**
     * Loads a VistaPerson from VistA given his/her DUZ.
     * @throws DataAccessException if communication with VistA failed
     * @throws IllegalArgumentException if the DUZ was not a valid DUZ
     */
    public VistaPerson loadVistaPerson(final String duz);
}

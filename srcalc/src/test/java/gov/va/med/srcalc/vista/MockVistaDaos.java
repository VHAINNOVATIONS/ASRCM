package gov.va.med.srcalc.vista;

import static org.mockito.Mockito.mock;

/**
 * Constructs mock VistA DAOs for testing. Check the below methods' Javadocs
 * to see whether the DAO methods will return realistic values.
 */
public class MockVistaDaos
{
    // TODO: add the patient DAO here?
    
    /**
     * Returns a mock {@link VistaSurgeryDao} using Mockito. All methods will
     * return the Mockito defaults.
     */
    public static VistaSurgeryDao mockSurgeryDao()
    {
        return mock(VistaSurgeryDao.class);
    }
}

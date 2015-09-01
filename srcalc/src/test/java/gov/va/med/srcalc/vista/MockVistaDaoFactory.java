package gov.va.med.srcalc.vista;

import static org.mockito.Mockito.*;

import javax.security.auth.login.LoginException;

import gov.va.med.srcalc.domain.calculation.SampleCalculations;

/**
 * Constructs mock VistA DAOs for testing. Check the below methods' Javadocs
 * to see whether the DAO methods will return realistic values.
 */
public class MockVistaDaoFactory implements VistaDaoFactory
{
    public static final String KNOWN_DIVISION = "442";

    private final VistaAuthenticator fAuthenticator;
    private final VistaPatientDao fPatientDao;
    private final VistaSurgeryDao fSurgeryDao;
    
    public MockVistaDaoFactory()
    {
        fPatientDao = mockPatientDao();
        fSurgeryDao = mockSurgeryDao();
        fAuthenticator = mock(VistaAuthenticator.class);
        try
        {
            when(fAuthenticator.authenticateViaAccessVerify(
                    anyString(), anyString(), anyString()))
                    .thenReturn(SampleCalculations.radiologistPerson());
            when(fAuthenticator.authenticateViaCcowToken(anyString(), anyString()))
                    .thenReturn(SampleCalculations.radiologistPerson());
        }
        catch (final LoginException ex)
        {
            // The compiler sees a possible LoginException, but it is just an artifact
            // of Mockito's mocking API.
            throw new RuntimeException("Unexpected Exception.", ex);
        }
    }
    
    @Override
    public boolean isDivisionKnown(final String division)
    {
        return KNOWN_DIVISION.equals(division);
    }

    /**
     * <p>Returns a mock VistaAuthenticator using Mockito. The mock has the following
     * stubs:</p>
     * 
     * <ul>
     * <li>Calling {@link VistaAuthenticator#authenticateViaAccessVerify(String, String,
     * String) authenticateViaAccessVerify} with any arguments will return {@link
     * SampleCalculations#radiologistPerson()}.</li>
     * <li>Calling {@link VistaAuthenticator#authenticateViaCcowToken(String, String)
     * authenticateViaCcowToken} with any arguments will return {@link
     * SampleCalculations#radiologistPerson()}.</li>
     * <li>Other methods return Mockito defaults.</li>
     * </ul>
     * 
     * <p>Repeated calls to this method will return the same object to allow mock
     * customization or verification.</p>
     */
    @Override
    public VistaAuthenticator getAuthenticator(final String division)
    {
        if (isDivisionKnown(division))
        {
            return fAuthenticator;
        }
        else
        {
            throw new IllegalArgumentException("unknown division");
        }
    }
    
    /**
     * 
     * <p>Returns a mock VistaPatientDao using Mockito. All VistaPatientDao methods will
     * return the Mockito defaults.</p>
     */
    public static VistaPatientDao mockPatientDao()
    {
        return mock(VistaPatientDao.class);
    }

    /**
     * <p>Returns a mock VistaPatientDao identical to {@link #mockPatientDao()}, but
     * repeated calls to this method will return the same object to allow mock
     * customization or verification.</p>
     */
    @Override
    public VistaPatientDao getVistaPatientDao()
    {
        return fPatientDao;
    }
    
    /**
     * <p>Returns a new mock VistaSurgeryDao using Mockito. All VistaSurgeryDao methods
     * will return the Mockito defaults.</p>
     */
    public static VistaSurgeryDao mockSurgeryDao()
    {
        return mock(VistaSurgeryDao.class);
    }

    /**
     * <p>Returns a mock VistaSurgeryDao identical to {@link #mockSurgeryDao()}, but
     * repeated calls to this method will return the same object to allow mock
     * customization or verification.</p>
     */
    public VistaSurgeryDao getVistaSurgeryDao()
    {
        return fSurgeryDao;
    }
}

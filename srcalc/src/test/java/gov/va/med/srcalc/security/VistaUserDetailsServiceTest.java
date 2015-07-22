package gov.va.med.srcalc.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.vista.*;

import org.junit.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Tests the {@link VistaUserDetailsService} class.
 */
public class VistaUserDetailsServiceTest
{
    private static final String DIVISION = "500";
    private static final String RADIOLOGIST_DUZ = "11716";
    private static final String UNKNOWN_DUZ = "11111";
    
    protected VistaPerson sampleRadiologist()
    {
        final VistaPerson radiologist = new VistaPerson(
                DIVISION, RADIOLOGIST_DUZ, "RADIOLOGIST,ONE", "unknown");
        return radiologist;
    }

    protected VistaPersonDao mockVistaDao()
    {
        final VistaPersonDao dao = mock(VistaPersonDao.class);
        when(dao.loadVistaPerson(RADIOLOGIST_DUZ)).thenReturn(sampleRadiologist());
        when(dao.loadVistaPerson(UNKNOWN_DUZ)).thenThrow(new IllegalArgumentException());
        return dao;
    }
    
    protected VistaDaoFactory mockVistaDaoFactory()
    {
        return new VistaDaoFactory()
        {
            @Override
            public VistaPersonDao getVistaPersonDao(String division)
            {
                return mockVistaDao();
            }
            
            @Override
            public VistaPatientDao getVistaPatientDao()
            {
                return new MockVistaPatientDao();
            }
            
            @Override
            public VistaSurgeryDao getVistaSurgeryDao()
            {
                return MockVistaDaos.mockSurgeryDao();
            }
        };
    }

    @Test
    public final void testLoadUserByUsername()
    {
        final VistaPerson samplePerson = sampleRadiologist();
        
        final VistaUserDetailsService service = new VistaUserDetailsService(mockVistaDaoFactory());
        final VistaUserDetails user = service.loadUserByUsername(samplePerson.getDuz());
        assertEquals(samplePerson.getDivision(), user.getDivision());
        assertEquals(samplePerson.getDuz(), user.getDuz());
        assertEquals(samplePerson.getDuz(), user.getUsername());
        assertEquals("", user.getPassword());
        assertEquals(samplePerson.getDisplayName(), user.getDisplayName());
        assertTrue("user does not have ROLE_USER",
                user.getAuthorities().contains(Roles.ROLE_USER.asGrantedAuthority()));
        assertFalse("user has ROLE_ADMIN",
                user.getAuthorities().contains(Roles.ROLE_ADMIN.asGrantedAuthority()));
    }
    
    @Test(expected = UsernameNotFoundException.class)
    public final void testLoadUnknownUser()
    {
        final VistaUserDetailsService service = new VistaUserDetailsService(mockVistaDaoFactory());
        service.loadUserByUsername(UNKNOWN_DUZ);
    }
    
}

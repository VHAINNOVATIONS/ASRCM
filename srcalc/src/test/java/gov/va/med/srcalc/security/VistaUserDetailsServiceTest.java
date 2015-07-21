package gov.va.med.srcalc.security;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.vista.*;

import org.junit.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Tests the {@link VistaUserDetailsService} class.
 */
public class VistaUserDetailsServiceTest
{
    private final MockVistaPersonDao fVistaPersonDao = new MockVistaPersonDao();
    
    protected VistaDaoFactory mockVistaDaoFactory()
    {
        return new VistaDaoFactory()
        {
            @Override
            public VistaPersonDao getVistaPersonDao(String division)
            {
                return fVistaPersonDao;
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
        final VistaPerson samplePerson = SampleCalculations.radiologistPerson();
        
        final VistaUserDetailsService service = new VistaUserDetailsService(mockVistaDaoFactory());
        final VistaUserDetails user = service.loadUserByUsername(samplePerson.getDuz());
        assertEquals(samplePerson.getStationNumber(), user.getDivision());
        assertEquals(samplePerson.getDuz(), user.getDuz());
        assertEquals(samplePerson.getDuz(), user.getUsername());
        assertEquals("", user.getPassword());
        assertEquals(samplePerson.getDisplayName(), user.getDisplayName());
        assertTrue("user should have ROLE_USER",
                user.getAuthorities().contains(Roles.ROLE_USER.asGrantedAuthority()));
        assertFalse("user should not have ROLE_ADMIN",
                user.getAuthorities().contains(Roles.ROLE_ADMIN.asGrantedAuthority()));
        assertTrue("user should be enabled", user.isEnabled());
        assertTrue("user should be non-expired", user.isAccountNonExpired());
        assertTrue("user should be non-locked", user.isAccountNonLocked());
        assertTrue("user credentials should be non-expired", user.isCredentialsNonExpired());
    }
    
    @Test(expected = UsernameNotFoundException.class)
    public final void testLoadUnknownUser()
    {
        final VistaUserDetailsService service = new VistaUserDetailsService(mockVistaDaoFactory());
        service.loadUserByUsername("11111");
    }
    
}

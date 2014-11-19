package gov.va.med.srcalc.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.vista.VistaDao;
import gov.va.med.srcalc.vista.VistaDaoFactory;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class VistaUserDetailsServiceTest
{
    final String RADIOLOGIST_DUZ = "11716";
    
    protected VistaPerson sampleRadiologist()
    {
        final VistaPerson radiologist = new VistaPerson(RADIOLOGIST_DUZ, "RADIOLOGIST,ONE", "unknown");
        return radiologist;
    }

    protected VistaDao mockVistaDao()
    {
        final VistaDao dao = mock(VistaDao.class);
        when(dao.loadVistaPerson(RADIOLOGIST_DUZ)).thenReturn(sampleRadiologist());
        return dao;
    }
    
    protected VistaDaoFactory mockVistaDaoFactory()
    {
        return new VistaDaoFactory()
        {
            @Override
            public VistaDao getVistaDao(String division)
            {
                return mockVistaDao();
            }
        };
    }

    @Test
    public final void testLoadUserByUsername()
    {
        final VistaPerson samplePerson = sampleRadiologist();
        
        final VistaUserDetailsService service = new VistaUserDetailsService(mockVistaDaoFactory());
        final VistaUserDetails user = service.loadUserByUsername(samplePerson.getDuz());
        assertEquals(samplePerson.getDuz(), user.getDuz());
        assertEquals(samplePerson.getDuz(), user.getUsername());
        assertEquals(samplePerson.getDisplayName(), user.getDisplayName());
        assertTrue("user does not have USER_ROLE",
                user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }
    
}

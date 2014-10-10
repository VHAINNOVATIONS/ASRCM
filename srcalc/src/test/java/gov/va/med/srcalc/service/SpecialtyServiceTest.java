package gov.va.med.srcalc.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Specialty;

import org.junit.Test;

public class SpecialtyServiceTest
{
    /**
     * Returns a basic set of Specialty objects.
     */
    public static List<Specialty> sampleSpecialtyList()
    {
        return Arrays.asList(
		    new Specialty(1, "General"),
		    new Specialty(2, "Neurosurgery"),
		    new Specialty(3, "Orthopedic"),
		    new Specialty(4, "Thoracic"),
		    new Specialty(5, "Urology"),
		    new Specialty(6, "Vascular"),
		    new Specialty(7, "Cardiac"),
		    new Specialty(8, "Other Non-Cardiac Specialty")
                );
    }

    /**
     * Simple test to ensure {@link DefaultSpecialtyService#getAllSpecialties()} does
     * its simple job.
     */
    @Test
    public void testGetAllSpecialties()
    {
        // Set up the mock dao.
        final SpecialtyDao dao = mock(SpecialtyDao.class);
        when(dao.getAllSpecialties()).thenReturn(sampleSpecialtyList());
        
        // The actual object we are testing.
        final DefaultSpecialtyService service = new DefaultSpecialtyService(dao);
        
        // Behavior verification.
        assertEquals(sampleSpecialtyList(), service.getAllSpecialties());
    }
    
}

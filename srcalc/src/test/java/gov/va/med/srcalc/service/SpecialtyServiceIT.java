package gov.va.med.srcalc.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>Integration Test for {@link SpecialtyService}. Unlike
 * {@link SpecialtyServiceTest}, this class tests real database interaction.</p>
 * 
 * <p>Note that we are testing the interface ({@link SpecialtyService}), not the
 * implementation ({@link DefaultSpecialtyService}). You can think of this IT
 * as testing the Spring-instantiated service bean, not a particular Java class.</p>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/srcalc-context.xml", "/test-context.xml"})
public class SpecialtyServiceIT
{
    @Autowired // TODO: do I even want to use autowiring in ITs?
    SpecialtyService fSpecialtyService;

    @Test
    public void testReturnsAllSpecialties()
    {
        assertEquals(
                SpecialtyServiceTest.sampleSpecialtyList().size(),
                fSpecialtyService.getAllSpecialties().size());
    }
}

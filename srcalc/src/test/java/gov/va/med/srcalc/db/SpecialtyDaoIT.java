package gov.va.med.srcalc.db;

import javax.inject.Inject;
import javax.transaction.Transactional;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.srcalc.service.CalculationServiceIT;
import gov.va.med.srcalc.test.util.DbUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration Test for {@link SpecialtyDao}. Only tests certain edge cases
 * because most cases are tested via {@link CalculationServiceIT} and others.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/srcalc-context.xml", "/test-context.xml"})
public class SpecialtyDaoIT
{
    @Inject
    SpecialtyDao fSpecialtyDao;
    
    /**
     * Verifies that {@link SpecialtyDao#getByName(String)} throws a
     * {@link ConfigurationException} for a misconfigured specialty.
     */
    @Test(expected = ConfigurationException.class)
    @Transactional
    public final void testMisconfiguredSpecialty()
    {
        // Put the Gender variable at a too-high index for the Vascular
        // specialty. (Remember that this will roll-back at the end of the test.)
        DbUtils.executeStatement(
                fSpecialtyDao.getCurrentSession(),
                "INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) " +
                "VALUES (6, 1, 80)");
        
        fSpecialtyDao.getByName("Vascular");
    }
    
}

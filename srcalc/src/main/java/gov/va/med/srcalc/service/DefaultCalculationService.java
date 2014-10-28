package gov.va.med.srcalc.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.workflow.NewCalculation;

public class DefaultCalculationService implements CalculationService
{
    private static final Logger fLogger = LoggerFactory.getLogger(DefaultCalculationService.class);
    
    private final SpecialtyDao fSpecialtyDao;
    
    @Inject
    public DefaultCalculationService(SpecialtyDao specialtyDao)
    {
        fSpecialtyDao = specialtyDao;
    }

    @Override
    @Transactional
    public NewCalculation startNewCalculation(int patientId)
    {
        final Patient patient = new Patient(patientId, "Dummy Patient"); //FIXME: fake

        fLogger.info("Starting calculation for patient {}.", patient);

        return new NewCalculation(
                Calculation.forPatient(patient),
                fSpecialtyDao.getAllSpecialties());
    }
    
}

package gov.va.med.srcalc.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.domain.variable.Value;
import gov.va.med.srcalc.domain.workflow.*;
import gov.va.med.srcalc.util.MissingValuesException;

public class DefaultCalculationService implements CalculationService
{
    private static final Logger fLogger = LoggerFactory.getLogger(DefaultCalculationService.class);
    
    private final SpecialtyDao fSpecialtyDao;
    
    @Inject
    public DefaultCalculationService(final SpecialtyDao specialtyDao)
    {
        fSpecialtyDao = specialtyDao;
    }

    @Override
    @Transactional
    public NewCalculation startNewCalculation(final int patientId)
    {
        final Patient patient = new Patient(patientId, "PATIENT,TEST" + patientId); //FIXME: fake

        fLogger.debug("Starting calculation for patient {}.", patient);

        return new NewCalculation(
                Calculation.forPatient(patient),
                fSpecialtyDao.getAllSpecialties());
    }
    
    @Override
    @Transactional
    public SelectedCalculation setSpecialty(final Calculation calculation, final String specialtyName)
        throws InvalidIdentifierException
    {
        fLogger.debug("Setting specialty to {}.", specialtyName);
        
        final Specialty specialty = fSpecialtyDao.getByName(specialtyName);
        if (specialty == null)
        {
            throw new InvalidIdentifierException(
                    specialtyName + " is not a valid specialty name.");
        }
        calculation.setSpecialty(specialty);

        return new SelectedCalculation(calculation);
    }
    
    @Override
    @Transactional
    public CalculationWorkflow runCalculation(final Calculation calculation, final List<Value> variableValues) throws MissingValuesException
    {
        fLogger.debug("Running calculation with values: {}", variableValues);
        
        try 
        {
			calculation.calculate(variableValues);
		} 
        catch (MissingValuesException e) 
        {
			throw e;
		}
        
        return new UnsignedCalculation(calculation);
    }
    
}

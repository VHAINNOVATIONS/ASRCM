package gov.va.med.srcalc.service;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.vista.*;
import gov.va.med.srcalc.vista.VistaPatientDao.SaveNoteCode;
import gov.va.med.srcalc.util.MissingValuesException;

/**
 * The canonical implementation of {@link CalculationService}.
 */
public class DefaultCalculationService implements CalculationService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCalculationService.class);
    
    private final SpecialtyDao fSpecialtyDao;
    private final VistaPatientDao fPatientDao;
    private final VistaSurgeryDao fSurgeryDao;
    
    /**
     * Constructs an instance.
     * @param specialtyDao DAO to access specialties
     * @param patientDao DAO to access patient information
     * @param surgeryDao DAO to save VistA Surgery information
     */
    @Inject
    public DefaultCalculationService(
            final SpecialtyDao specialtyDao,
            final VistaPatientDao patientDao,
            final VistaSurgeryDao surgeryDao)
    {
        fSpecialtyDao = specialtyDao;
        fPatientDao = patientDao;
        fSurgeryDao = surgeryDao;
    }
    
    @Override
    @Transactional
    public List<Specialty> getValidSpecialties()
    {
        return fSpecialtyDao.getAllSpecialties();
    }

    @Override
    @Transactional
    public Calculation startNewCalculation(final int patientId)
    {
        final Patient patient = fPatientDao.getPatient(patientId);

        LOGGER.debug("Starting calculation for patient {}.", patient);

        return Calculation.forPatient(patient);
    }
    
    @Override
    @Transactional
    public void setSpecialty(final Calculation calculation, final String specialtyName)
        throws InvalidIdentifierException
    {
        LOGGER.debug("Setting specialty to {}.", specialtyName);
        
        final Specialty specialty = fSpecialtyDao.getByName(specialtyName);
        if (specialty == null)
        {
            throw new InvalidIdentifierException(
                    specialtyName + " is not a valid specialty name.");
        }
        calculation.setSpecialty(specialty);
    }
    
    @Override
    @Transactional
    public CalculationResult runCalculation(
            final Calculation calculation, final Collection<Value> variableValues)
            throws MissingValuesException
    {
        LOGGER.debug("Running calculation with values: {}", variableValues);
        
        final CalculationResult result = calculation.calculate(variableValues);
        
        // Log something at INFO level for running a calculation, but don't log
        // too much to avoid PHI in the log file.
        LOGGER.info( "Ran a {} calculation.", calculation.getSpecialty());
        
        return result;
    }

    @Override
    public VistaPatientDao.SaveNoteCode signRiskCalculation(
            CalculationResult result, String electronicSignature)
    {
        final VistaPatientDao.SaveNoteCode returnCode = 
            fPatientDao.saveRiskCalculationNote(
                result.getPatientDfn(), electronicSignature, result.buildNoteBody());
        if (returnCode == SaveNoteCode.SUCCESS)
        {
            final SignedResult signedResult = result.signed();
            fSurgeryDao.saveCalculationResult(signedResult);
        }
        return returnCode;
    }
    
}

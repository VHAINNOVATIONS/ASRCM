package gov.va.med.srcalc.service;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import gov.va.med.srcalc.db.ResultsDao;
import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.MissingValuesException;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.vista.*;
import gov.va.med.srcalc.vista.VistaPatientDao.SaveNoteCode;
import gov.va.med.srcalc.security.SecurityUtil;

/**
 * The canonical implementation of {@link CalculationService}.
 */
public class DefaultCalculationService implements CalculationService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCalculationService.class);
    
    private final SpecialtyDao fSpecialtyDao;
    private final VistaPatientDao fPatientDao;
    private final VistaSurgeryDao fSurgeryDao;
    private final ResultsDao fResultsDao;
    
    /**
     * Constructs an instance.
     * @param specialtyDao DAO to access specialties
     * @param patientDao DAO to access patient information
     * @param surgeryDao DAO to save VistA Surgery information
     * @param resultsDao DAO to save calculation results
     */
    @Inject
    public DefaultCalculationService(
            final SpecialtyDao specialtyDao,
            final VistaPatientDao patientDao,
            final VistaSurgeryDao surgeryDao,
            final ResultsDao resultsDao)
    {
        fSpecialtyDao = specialtyDao;
        fPatientDao = patientDao;
        fSurgeryDao = surgeryDao;
        fResultsDao = resultsDao;
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
        
        final boolean firstRun = !calculation.getHistoricalCalculation().isPresent();

        final VistaPerson user = SecurityUtil.getCurrentPrincipal().getVistaPerson();
        final CalculationResult result = calculation.calculate(variableValues, user);
        
        // If it was the first run, save the historical result for metrics.
        if (firstRun)
        {
            LOGGER.debug("Was first run: saving HistoricalCalculation.");
            fResultsDao.persistHistoricalCalc(calculation.getHistoricalCalculation().get());
        }
        
        // Log something at INFO level for running a calculation, but don't log
        // too much to avoid PHI in the log file.
        LOGGER.info( "Ran a {} calculation.", calculation.getSpecialty());
        
        return result;
    }

    @Override
    @Transactional
    public VistaPatientDao.SaveNoteCode signRiskCalculation(
            CalculationResult result, String electronicSignature)
    {
        // Note: we must save the note first because this is how we check the given
        // signature code.
        final VistaPatientDao.SaveNoteCode returnCode = 
            fPatientDao.saveRiskCalculationNote(
                result.getPatientDfn(), electronicSignature, result.buildNoteBody());

        if (returnCode == SaveNoteCode.SUCCESS)
        {
            final SignedResult signedResult = result.signed();
            // Save to the DB first because the DB can rollback if VistA fails. (VistA
            // can't rollback if DB fails.)
            fResultsDao.persistSignedResult(signedResult);
            fSurgeryDao.saveCalculationResult(signedResult);
            
            // Log something at INFO level for signing a calculation, but don't log
            // too much to avoid PHI in the log file.
            LOGGER.info("Signed a {} calculation.", result.getSpecialtyName());
        }
        return returnCode;
    }
    
}

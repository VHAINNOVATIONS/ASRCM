package gov.va.med.srcalc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import gov.va.med.srcalc.db.SpecialtyDao;
import gov.va.med.srcalc.domain.Calculation;
import gov.va.med.srcalc.domain.Patient;
import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.domain.variable.MultiSelectOption;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable.DisplayType;
import gov.va.med.srcalc.domain.variable.NumericalVariable;
import gov.va.med.srcalc.domain.variable.Variable;
import gov.va.med.srcalc.domain.workflow.NewCalculation;
import gov.va.med.srcalc.domain.workflow.SelectedCalculation;

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
    
    @Override
    @Transactional
    public SelectedCalculation setSpecialty(Calculation calculation, String specialtyName)
        throws InvalidIdentifierException
    {
        fLogger.info("Setting specialty to {}.", specialtyName);
        
        final Specialty specialty = fSpecialtyDao.getByName(specialtyName);
        if (specialty == null)
        {
            throw new InvalidIdentifierException(
                    specialtyName + " is not a valid specialty name.");
        }
        calculation.setSpecialty(specialty);
        
        // FIXME: totally fake
        final ArrayList<Variable> variables = new ArrayList<>();
        if (specialtyName.equals("Cardiac"))
        {
            List<MultiSelectOption> options = Arrays.asList(
                    new MultiSelectOption("Male"),
                    new MultiSelectOption("Female")
                    );
            variables.add(new MultiSelectVariable("Gender", DisplayType.Radio, true, options));
        }
        else //non-cardiac
        {
            variables.add(new NumericalVariable("Age", true));
        }

        return new SelectedCalculation(calculation, variables);
    }
    
}

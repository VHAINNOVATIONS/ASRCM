package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.ModelTerm;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import java.util.HashSet;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EditSpecialtyValidator implements Validator
{
    private final ModelInspectionService fModelService;
    
    public EditSpecialtyValidator(final ModelInspectionService modelService)
    {
        fModelService = modelService;
    }
    
    /**
     * Returns true if, and only if, the given class is {@link EditSpecialty} or a subclass.
     */
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return EditSpecialty.class.isAssignableFrom(clazz);
    }

    /**
     * Validates the given object, using error codes from {@link ValidationCodes}.
     * @param obj the object to validate. Must be an instance of {@link EditSpecialty}.
     * @throws ClassCastException if the given object is not.
     */
    @Override
    public void validate(final Object obj, final Errors e)
    {
        // Note: the editSpecialty object is accessible from the e Error object.
        //
        ValidationUtils.rejectIfEmpty(e, "name", ValidationCodes.NO_VALUE);
        ValidationUtils2.rejectIfTooLong(e, "name", DisplayNameConditions.DISPLAY_NAME_MAX );
        
        ValidationUtils2.rejectIfDoesntMatch(
                e, "name", Specialty.VALID_SPECIALTY_NAME_PATTERN,
                new Object[] {Specialty.VALID_SPECIALTY_NAME_CHARACTERS});                        
    }
}

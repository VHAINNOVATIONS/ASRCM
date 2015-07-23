package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * A (@link Validator) object for validating (@link EditSpecialty) objects.
 */
public class EditSpecialtyValidator implements Validator
{    
    public EditSpecialtyValidator()
    {
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
        
        // Note: Specialties have a different max name length than models, variables...
        ValidationUtils2.rejectIfTooLong(e, "name", Specialty.SPECIALTY_NAME_MAX );
        
        ValidationUtils2.rejectIfDoesntMatch(
                e, "name", Specialty.VALID_SPECIALTY_NAME_PATTERN,
                new Object[] {Specialty.VALID_SPECIALTY_NAME_CHARACTERS});                        
    }
}

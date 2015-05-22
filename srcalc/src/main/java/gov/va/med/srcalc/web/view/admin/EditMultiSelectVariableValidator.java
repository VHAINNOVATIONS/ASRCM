package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.validation.*;

/**
 * Validates an {@link EditMultiSelectVariable} object.
 */
public class EditMultiSelectVariableValidator implements Validator
{
    private final EditVariableValidator fBaseValidator = new EditVariableValidator();
    
    /**
     * Returns true if (and only if) the given class is EditMultiSelectVariable
     * or a subclass.
     */
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return EditMultiSelectVariable.class.isAssignableFrom(clazz);
    }
    
    /**
     * Validates the given object, using error codes from {@link ValidationCodes}.
     * @param obj the object to validate. Must be an instance of {@link EditMultiSelectVariable}.
     * @throws ClassCastException if the given object is not an EditMultiSelectVariable
     */
    @Override
    public void validate(final Object target, final Errors errors)
    {
        final EditMultiSelectVariable editVariable = (EditMultiSelectVariable)target;
        // First, delegate to EditVariableValidator for validating the basic
        // properties.
        fBaseValidator.validate(target, errors);
        
        // Validate displayType.
        ValidationUtils.rejectIfEmpty(
                errors, "displayType", ValidationCodes.NO_VALUE);
        
        // Validate options
        if (editVariable.getOptions().isEmpty())
        {
            errors.rejectValue(
                    "options",
                    ValidationCodes.NO_VALUE,
                    "No options specified.");
        }
        // Iterate using the index here because we need it to specify the field.
        for (int i = 0; i < editVariable.getOptions().size(); ++i)
        {
            final String fieldName = String.format("options[%d]", i);
            ValidationUtils.rejectIfEmpty(errors, fieldName, ValidationCodes.NO_VALUE);
            ValidationUtils2.rejectIfTooLong(errors, fieldName, MultiSelectOption.VALUE_MAX);
            ValidationUtils2.rejectIfDoesntMatch(
                    errors, fieldName, MultiSelectOption.VALID_VALUE_PATTERN,
                    new Object[] {MultiSelectOption.VALID_VALUE_CHARACTERS});
        }
    }
    
}

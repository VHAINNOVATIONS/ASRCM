package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.validation.*;

import com.google.common.collect.ImmutableList;

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
        
        // Validate options. Use the getTrimmedOptions since that is what
        // getMultiSelectOptions will ultimately use. (That is, trailing blanks
        // are omitted.)
        final ImmutableList<String> options = editVariable.getTrimmedOptions();
        if (options.isEmpty())
        {
            errors.rejectValue(
                    "options",
                    ValidationCodes.NO_VALUE,
                    "No options specified.");
        }
        else if (options.size() > editVariable.getMaxOptions())
        {
            errors.rejectValue(
                    "options",
                    ValidationCodes.TOO_LONG,
                    new Object[] { editVariable.getMaxOptions() },
                    "too many options");
        }
        // Iterate using the index here because we need it to specify the field.
        for (int i = 0; i < options.size(); ++i)
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

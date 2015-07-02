package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.validation.*;

import com.google.common.collect.ImmutableList;

/**
 * Validates an {@link EditMultiSelectVar} object.
 */
public class EditMultiSelectVarValidator implements Validator
{
    private final EditBaseVarValidator fBaseValidator = new EditBaseVarValidator();
    
    /**
     * Returns true if (and only if) the given class is {@link EditMultiSelectVar}
     * or a subclass.
     */
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return EditMultiSelectVar.class.isAssignableFrom(clazz);
    }
    
    /**
     * Validates the given object, using error codes from {@link ValidationCodes}.
     * @param target the object to validate. Must be an instance of {@link
     * EditMultiSelectVar}.
     * @throws ClassCastException if the given object is not an EditMultiSelectVar
     */
    @Override
    public void validate(final Object target, final Errors errors)
    {
        final EditMultiSelectVar editVar = (EditMultiSelectVar)target;
        // First, delegate to EditBaseVarValidator for validating the basic
        // properties.
        fBaseValidator.validate(target, errors);
        
        // Validate displayType.
        ValidationUtils.rejectIfEmpty(
                errors, "displayType", ValidationCodes.NO_VALUE);
        
        // Validate options. Use the getTrimmedOptions since that is what
        // getMultiSelectOptions will ultimately use. (That is, trailing blanks
        // are omitted.)
        final ImmutableList<String> options = editVar.getTrimmedOptions();
        if (options.isEmpty())
        {
            errors.rejectValue(
                    "options",
                    ValidationCodes.NO_VALUE,
                    "No options specified.");
        }
        else if (options.size() > editVar.getMaxOptions())
        {
            errors.rejectValue(
                    "options",
                    ValidationCodes.TOO_LONG,
                    new Object[] { editVar.getMaxOptions() },
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

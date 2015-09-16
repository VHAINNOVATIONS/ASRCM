package gov.va.med.srcalc.web.view.admin;

import java.util.List;

import gov.va.med.srcalc.domain.model.AbstractNumericalVariable;
import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.validation.*;

/**
 * Validates an {@link EditDiscreteNumericalVar} object.
 */
public class EditDiscreteNumericalVarValidator implements Validator
{
    private final EditBaseVarValidator fBaseValidator = new EditBaseVarValidator();
    
    /**
     * Returns true if (and only if) the given class is {@link
     * EditDiscreteNumericalVar} or a subclass.
     */
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return EditDiscreteNumericalVar.class.isAssignableFrom(clazz);
    }
    
    /**
     * Validates the given object, using error codes from {@link ValidationCodes}.
     * @param target the object to validate. Must be an instance of {@link
     * EditDiscreteNumericalVar}.
     * @throws ClassCastException if the given object is not an EditDiscreteNumericalVar
     */
    @Override
    public void validate(final Object target, final Errors errors)
    {
        final EditDiscreteNumericalVar editVar = (EditDiscreteNumericalVar)target;
        // First, delegate to EditBaseVarValidator for validating the basic
        // properties.
        fBaseValidator.validate(target, errors);
        
        // Validate units string
        ValidationUtils2.rejectIfTooLong(errors, "units", editVar.getUnitsMax());
        ValidationUtils2.rejectIfDoesntMatch(
                errors,
                "units",
                AbstractNumericalVariable.VALID_UNITS_PATTERN,
                new Object[] {AbstractNumericalVariable.VALID_UNITS_CHARACTERS});
        
        // The range doesn't need explicit validation: the only error could be
        // converting from strings to numbers, but Spring will detect that.

        // Validate categories
        final List<CategoryBuilder> categories = editVar.getTrimmedCategories();
        if (categories.size() < editVar.getMinCategories())
        {
            errors.rejectValue(
                    "categories",
                    ValidationCodes.TOO_SHORT,
                    new Object[] {editVar.getMinCategories()},
                    "too few categories specified");
        }
        else if (categories.size() > editVar.getMaxCategories())
        {
            errors.rejectValue(
                    "categories",
                    ValidationCodes.TOO_LONG,
                    new Object[] {editVar.getMaxCategories()},
                    "too many categories specified");
        }
        // Iterate using the index here because we need it to specify the field.
        for (int i = 0; i < categories.size(); ++i)
        {
            final String fieldName = String.format("categories[%d]", i);
            final String valueFieldName = fieldName + ".value";
            ValidationUtils.rejectIfEmpty(errors, valueFieldName, ValidationCodes.NO_VALUE);
            ValidationUtils2.rejectIfTooLong(errors, valueFieldName, MultiSelectOption.VALUE_MAX);
            ValidationUtils2.rejectIfDoesntMatch(
                    errors, valueFieldName, MultiSelectOption.VALID_VALUE_PATTERN,
                    new Object[] {MultiSelectOption.VALID_VALUE_CHARACTERS});
            // Validate if the category matches a previous category's name.
            // Category names should be unique and non-empty.
            // Check if there was an equal CategoryBuilder before this one.
            if(!categories.get(i).getValue().equals("") && 
                    categories.indexOf(categories.get(i)) != i)
            {
                errors.rejectValue(valueFieldName, ValidationCodes.DUPLICATE_VALUE);
            }
        }
    }
    
}

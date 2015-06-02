package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.AbstractNumericalVariable;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
     * @param obj the object to validate. Must be an instance of {@link EditDiscreteNumericalVar}.
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

        // TODO: categories
    }
    
}

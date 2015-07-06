package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.validation.*;

/**
 * This Validator validates the {@link EditBaseVar} properties. Other validators
 * validate subclasses' properties.
 */
public class EditBaseVarValidator implements Validator
{
    /**
     * Returns true if, and only if, the given class is {@link EditBaseVar} or
     * a subclass.
     */
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return EditBaseVar.class.isAssignableFrom(clazz);
    }

    /**
     * Validates the given object, using error codes from {@link ValidationCodes}.
     * @param obj the object to validate. Must be an instance of {@link EditBaseVar}.
     * @throws ClassCastException if the given object is not an EditBaseVar
     */
    @Override
    public void validate(final Object obj, final Errors e)
    {
        // See method Javadoc: we assume that it's an instance of EditBaseVar.
        final EditBaseVar editVar = (EditBaseVar)obj;
        
        // Validate variable key constraints. See AbstractVariable.getKey().
        ValidationUtils.rejectIfEmpty(e, "key", ValidationCodes.NO_VALUE);
        ValidationUtils2.rejectIfTooLong(e, "key", AbstractVariable.KEY_MAX);
        ValidationUtils2.rejectIfDoesntMatch(
                e, "key", AbstractVariable.VALID_KEY_PATTERN,
                new Object[] {AbstractVariable.VALID_KEY_CHARACTERS});

        // Validate displayName constraints. See
        // AbstractVariable.setDisplayName().
        ValidationUtils.rejectIfEmpty(e, "displayName", ValidationCodes.NO_VALUE);
        ValidationUtils2.rejectIfTooLong(e, "displayName", DisplayNameConditions.DISPLAY_NAME_MAX);
        ValidationUtils2.rejectIfDoesntMatch(
                e, "displayName", DisplayNameConditions.VALID_DISPLAY_NAME_PATTERN,
                new Object[] {DisplayNameConditions.VALID_DISPLAY_NAME_CHARACTERS});
        
        // Ensure we have a valid VariableGroup ID.
        if (!editVar.getGroup().isPresent())
        {
            e.rejectValue("groupId", ValidationCodes.INVALID_OPTION);
        }
        
        // Validate helpText constraints. See AbstractVariable.setHelpText().
        ValidationUtils2.rejectIfTooLong(e, "helpText", AbstractVariable.HELP_TEXT_MAX);
    }
    
}

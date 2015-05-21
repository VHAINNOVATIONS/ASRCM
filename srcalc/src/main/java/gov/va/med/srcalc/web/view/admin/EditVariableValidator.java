package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.domain.model.Variable;

import org.springframework.validation.*;

/**
 * This Validator validates the base {@link EditVariable} properties. Other
 * validators validate subclasses' properties.
 */
public class EditVariableValidator implements Validator
{
    /**
     * Error code used when a required value is not provided.
     */
    public static final String ERROR_NO_VALUE = "noInput";

    /**
     * Error code used when a String value is longer than the maximum length.
     */
    public static final String ERROR_TOO_LONG = "tooLong";
    
    /**
     * Error code used when an invalid value is given for a multi-select option.
     */
    public static final String ERROR_INVALID_OPTION = "invalidOption";
    
    /**
     * Error code used when a String value contains invalid characters.
     */
    public static final String ERROR_INVALID_CONTENTS = "invalidContents";
    
    /**
     * Returns true if, and only if, the given class is {@link EditVariable} or
     * a subclass.
     */
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return EditVariable.class.isAssignableFrom(clazz);
    }

    /**
     * @param obj the object to validate. Must be an instance of {@link EditVariable}.
     * @throws ClassCastException if the given object is not an EditVariable
     */
    @Override
    public void validate(final Object obj, final Errors e)
    {
        // See method Javadoc: we assume that it's an instance of EditVariable.
        final EditVariable editVariable = (EditVariable)obj;

        // Validate displayName constraints. See
        // AbstractVariable.setDisplayName().
        ValidationUtils.rejectIfEmpty(e, "displayName", ERROR_NO_VALUE);
        final String displayName = editVariable.getDisplayName();
        if (displayName.length() > AbstractVariable.DISPLAY_NAME_MAX)
        {
            e.rejectValue(
                    "displayName",
                    ERROR_TOO_LONG,
                    new Object[] {AbstractVariable.DISPLAY_NAME_MAX},
                    "The display name is too long.");
        }
        if (!AbstractVariable.VALID_DISPLAY_NAME_PATTERN.matcher(displayName).matches())
        {
            e.rejectValue(
                    "displayName",
                    ERROR_INVALID_CONTENTS,
                    new Object[] {AbstractVariable.VALID_DISPLAY_NAME_CHARACTERS},
                    "invalid characters in string");
        }
        
        // Ensure we have a valid VariableGroup ID.
        if (!editVariable.getGroup().isPresent())
        {
            e.rejectValue("groupId", ERROR_INVALID_OPTION);
        }
        
        // Validate helpText constraints. See AbstractVariable.setHelpText().
        final String helpText = editVariable.getHelpText();
        if (helpText.length() > Variable.HELP_TEXT_MAX)
        {
            e.rejectValue(
                    "helpText",
                    ERROR_TOO_LONG,
                    new Object[] {Variable.HELP_TEXT_MAX},
                    "too long");
        }
    }
    
}

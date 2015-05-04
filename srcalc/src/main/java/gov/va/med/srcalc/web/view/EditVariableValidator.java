package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.AbstractVariable;

import org.springframework.validation.*;

/**
 * This Validator validates {@link EditVariable} instances.
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
    
    @Override
    public boolean supports(Class<?> clazz)
    {
        return EditVariable.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e)
    {
        final EditVariable editVariable = (EditVariable)obj;
        ValidationUtils.rejectIfEmpty(e, "displayName", ERROR_NO_VALUE);
        if (editVariable.getDisplayName().length() > AbstractVariable.DISPLAY_NAME_MAX)
        {
            e.rejectValue(
                    "displayName",
                    ERROR_TOO_LONG,
                    new Object[] {AbstractVariable.DISPLAY_NAME_MAX},
                    "The display name is too long.");
        }
        
        if (!editVariable.getGroup().isPresent())
        {
            e.rejectValue("groupId", ERROR_INVALID_OPTION);
        }
    }
    
}

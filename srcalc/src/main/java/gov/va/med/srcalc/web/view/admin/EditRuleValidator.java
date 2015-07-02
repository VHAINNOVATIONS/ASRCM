package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validates an {@link EditRule} object.
 */
public class EditRuleValidator implements Validator
{

    /**
     * Returns true if (and only if) the given class is {@link
     * EditRule} or a subclass.
     */
    @Override
    public boolean supports(Class<?> clazz)
    {
        return clazz.isAssignableFrom(EditRule.class);
    }

    /**
     * Validates the given EditRule object, using error codes from {@link ValidationCodes}.
     * @param obj the object to validate. Must be an instance of {@link EditRule}.
     * @throws ClassCastException if the given object is not an EditRule
     */
    @Override
    public void validate(final Object target, final Errors errors)
    {
        final EditRule editRule = (EditRule) target;
        
        // Validate displayName constraints. See
        // AbstractVariable.setDisplayName() because this display name obeys the
        // same constraints.
        ValidationUtils.rejectIfEmpty(errors, "displayName", ValidationCodes.NO_VALUE);
        ValidationUtils2.rejectIfTooLong(errors, "displayName", DisplayNameConditions.DISPLAY_NAME_MAX);
        ValidationUtils2.rejectIfDoesntMatch(
                errors, "displayName", DisplayNameConditions.VALID_DISPLAY_NAME_PATTERN,
                new Object[] {DisplayNameConditions.VALID_DISPLAY_NAME_CHARACTERS});
        ValidationUtils.rejectIfEmpty(errors, "summandExpression", ValidationCodes.NO_VALUE);
        //Validate the rule's summand expression
        final SpelExpressionParser parser = new SpelExpressionParser();
        try
        {
            parser.parseExpression(editRule.getSummandExpression());
        }
        catch(final ParseException e)
        {
            errors.rejectValue("summandExpression", ValidationCodes.INVALID_EXPRESSION);
        }
        
        //Validate each value matcher expression
        for(int i = 0; i < editRule.getMatchers().size(); i++)
        {
            final ValueMatcherBuilder currentMatcher = editRule.getMatchers().get(i);
            // If an expression is enabled, it must have a non-empty expression.
            if(currentMatcher.isEnabled())
            {
                ValidationUtils.rejectIfEmpty(
                        errors,
                        String.format("matchers[%d].booleanExpression", i),
                        ValidationCodes.NO_VALUE);
            }
            try
            {
                parser.parseExpression(editRule.getMatchers().get(i).getBooleanExpression());
            }
            catch(final ParseException e)
            {
                errors.rejectValue(String.format("matchers[%d].booleanExpression", i),
                        ValidationCodes.INVALID_EXPRESSION);
            }
        }
    }
    
}

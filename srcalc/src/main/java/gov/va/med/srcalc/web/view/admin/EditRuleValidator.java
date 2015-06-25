package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EditRuleValidator implements Validator
{

    @Override
    public boolean supports(Class<?> clazz)
    {
        // TODO: Investigate if this should be changed to equals since we 
        // most likely will not have any subclasses of EditRule
        return clazz.isAssignableFrom(EditRule.class);
    }

    @Override
    public void validate(final Object target, final Errors errors)
    {
        final EditRule editRule = (EditRule) target;
        
        // Validate displayName constraints. See
        // AbstractVariable.setDisplayName() because this display name obeys the
        // same constraints.
        ValidationUtils.rejectIfEmpty(errors, "displayName", ValidationCodes.NO_VALUE);
        ValidationUtils2.rejectIfTooLong(errors, "displayName", AbstractVariable.DISPLAY_NAME_MAX);
        ValidationUtils2.rejectIfDoesntMatch(
                errors, "displayName", AbstractVariable.VALID_DISPLAY_NAME_PATTERN,
                new Object[] {AbstractVariable.VALID_DISPLAY_NAME_CHARACTERS});
        //Validate the rule's summand expression
        final SpelExpressionParser parser = new SpelExpressionParser();
        try
        {
            parser.parseExpression(editRule.getSummandExpression());
        }
        catch(final ParseException e)
        {
            errors.rejectValue("summandExpression", ValidationCodes.INVALID_SUMMAND_EXPRESSION);
        }
        
        //Validate each value matcher expression
        for(int i = 0; i < editRule.getMatchers().size(); i++)
        {
            try
            {
                parser.parseExpression(editRule.getMatchers().get(i).getBooleanExpression());
            }
            catch(final ParseException e)
            {
                errors.rejectValue(String.format("matchers[%d]", i),
                        ValidationCodes.INVALID_MATCHER_EXPRESSION);
            }
        }
    }
    
}

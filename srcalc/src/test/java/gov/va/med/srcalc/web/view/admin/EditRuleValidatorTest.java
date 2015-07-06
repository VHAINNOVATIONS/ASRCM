package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.test.util.TestHelpers;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.ValidationCodes;

public class EditRuleValidatorTest
{
    private final ModelInspectionService fAdminService = new MockModelService();
    
    private static final String INVALID_EXPRESSION = "Invalid asdfjasidfj@#@#%";
    
    private static final String DUMMY_VAR_KEY = "dummyKey";
    
    /**
     * Instantiates a basic {@link EditRule} instance.
     */
    private EditRule makeEditRule()
    {
        // Use the predefined sample rule
        return new EditRule(fAdminService, SampleModels.ageAndFsRule());
    }
    
    /**
     * Validates the given object and returns the binding result.
     */
    private BeanPropertyBindingResult validate(final EditRule editRule)
    {
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(editRule, "rule");
        final Validator validator = editRule.getValidator();
        assertTrue(validator.supports(editRule.getClass()));
        validator.validate(editRule, errors);
        return errors;
    }
    
    @Test
    public final void testValid()
    {
        final BindingResult errors = validate(makeEditRule());

        assertEquals("error count", 0, errors.getErrorCount());
    }
    
    @Test
    public final void testEmptyDisplayName()
    {
        final EditRule editRule = makeEditRule();
        editRule.setDisplayName("");
        final BindingResult errors = validate(editRule);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(ValidationCodes.NO_VALUE, errors.getFieldError("displayName").getCode());
    }
    
    @Test
    public final void testDisplayNameTooLong()
    {
        final EditRule editRule = makeEditRule();
        editRule.setDisplayName(TestHelpers.stringOfLength(DisplayNameConditions.DISPLAY_NAME_MAX + 1));
        final BindingResult errors = validate(editRule);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(ValidationCodes.TOO_LONG, errors.getFieldError("displayName").getCode());
    }
    
    @Test
    public final void testInvalidSummandExpression()
    {
        final EditRule editRule = makeEditRule();
        editRule.setDisplayName("Display this");
        editRule.setSummandExpression(INVALID_EXPRESSION);
        final BindingResult errors = validate(editRule);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(ValidationCodes.INVALID_EXPRESSION,
                errors.getFieldError("summandExpression").getCode());
    }
    
    @Test
    public final void testInvalidMatcher()
    {
        final EditRule editRule = makeEditRule();
        editRule.setDisplayName("Display this");
        editRule.getMatchers().add(new ValueMatcherBuilder(DUMMY_VAR_KEY)
            .setBooleanExpression(INVALID_EXPRESSION));
        editRule.getMatchers().add(new ValueMatcherBuilder(DUMMY_VAR_KEY)
                .setBooleanExpression("true 1234"));
        final BindingResult errors = validate(editRule);
        
        assertEquals(2, errors.getErrorCount());
        // There are already two matchers for the age and functional status rule
        assertEquals(ValidationCodes.INVALID_EXPRESSION,
                errors.getFieldError("matchers[2].booleanExpression").getCode());
    }
}

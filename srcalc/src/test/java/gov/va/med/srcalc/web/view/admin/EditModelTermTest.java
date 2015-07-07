package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.util.SimpleFieldError;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.web.view.admin.EditModelTerm.TermType;

import org.junit.Test;
import org.springframework.validation.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Tests {@link EditModelTerm}, including validation since the validators are not exposed
 * as standalone classes.
 */
public class EditModelTermTest
{
    final MockModelService fModelService = new MockModelService();
    
    /**
     * Validates the given EditModelTerm and returns the Errors object containing the
     * validation errors, if any.
     */
    private Errors validate(final EditModelTerm editTerm)
    {
        final Validator validator = editTerm.getValidator(fModelService);
        assertTrue(validator.supports(editTerm.getClass()));
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(editTerm, "editTerm");
        validator.validate(editTerm, errors);
        return errors;
    }
    
    private static Set<SimpleFieldError> toSimpleErrors(final Collection<FieldError> errors)
    {
        final HashSet<SimpleFieldError> simpleErrors = new HashSet<>(errors.size());
        for (final FieldError error: errors)
        {
            simpleErrors.add(SimpleFieldError.fromFieldError(error));
        }
        return simpleErrors;
    }
    
    /**
     * Asserts that the given Errors object has only given given errors.
     */
    private static void assertOnlyTheseErrors(
            final Errors errorsObject,
            final SimpleFieldError... simpleErrors)
    {
        assertEquals(ImmutableList.of(), errorsObject.getGlobalErrors());
        assertEquals(
                ImmutableSet.copyOf(simpleErrors),
                toSimpleErrors(errorsObject.getFieldErrors()));
    }
    
    @Test
    public final void testBuildNewConstant() throws InvalidIdentifierException
    {
        final float coeff = 3.01f;
        final ConstantTerm expected = new ConstantTerm(coeff);
        
        // Basic properties
        final EditModelTerm editTerm = EditModelTerm.forConstant(coeff);
        assertEquals(TermType.CONSTANT, editTerm.getTermType());
        assertEquals(coeff, editTerm.getCoefficient(), 0.0f);
        
        // Validation
        final Errors errors = validate(editTerm);
        assertEquals(ImmutableList.of(), errors.getAllErrors());
        
        // Build
        assertEquals(expected, editTerm.build(fModelService));
    }
    
    @Test
    public final void testConstantWithKey()
    {
        final EditModelTerm editTerm = EditModelTerm.forConstant(1.0f);
        assertSame(editTerm, editTerm.setKey("akey"));
        
        // Validation
        final Errors errors = validate(editTerm);
        assertOnlyTheseErrors(
                errors,
                new SimpleFieldError("key", ValidationCodes.NOT_APPLICABLE));
    }
    
    @Test
    public final void testConstantWithOptionValue()
    {
        final EditModelTerm editTerm = EditModelTerm.forConstant(1.0f);
        assertSame(editTerm, editTerm.setOptionValue("opt"));
        
        // Validation
        final Errors errors = validate(editTerm);
        assertOnlyTheseErrors(
                errors,
                new SimpleFieldError("optionValue", ValidationCodes.NOT_APPLICABLE));
    }
    
    @Test
    public final void testBuildNewDiscreteNumericalVariable()
            throws InvalidIdentifierException
    {
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        final float coeff = 3.7f;
        final DiscreteTerm expected = new DiscreteTerm(var, 1, coeff);
        final String optionValue =
                var.getOptions().get(expected.getOptionIndex()).getValue();
        
        // Basic properties
        final EditModelTerm editTerm = EditModelTerm.forVariable("wbc", coeff);
        assertSame(editTerm, editTerm.setOptionValue(optionValue));
        assertEquals(TermType.VARIABLE, editTerm.getTermType());
        assertEquals(var.getKey(), editTerm.getKey());
        assertEquals(optionValue, editTerm.getOptionValue());
        assertEquals(coeff, editTerm.getCoefficient(), 0.0f);
        
        // Validation
        final Errors errors = validate(editTerm);
        assertEquals(ImmutableList.of(), errors.getAllErrors());
        
        // Build
        assertEquals(expected, editTerm.build(fModelService));
    }
    
    @Test
    public final void testVariableNoKey()
    {
        final EditModelTerm editTerm = new EditModelTerm();
        editTerm.setTermType(TermType.VARIABLE);
        editTerm.setKey("");
        editTerm.setCoefficient(0.01f);
        
        // Validation
        final Errors errors = validate(editTerm);
        assertOnlyTheseErrors(
                errors, new SimpleFieldError("key", ValidationCodes.NO_VALUE));
    }
    
    @Test
    public final void testBooleanVariableWithOption()
    {
        final EditModelTerm editTerm = EditModelTerm.forVariable("dnr", 0.0045f);
        editTerm.setOptionValue("option");
        
        // Validation
        final Errors errors = validate(editTerm);
        assertOnlyTheseErrors(
                errors,
                new SimpleFieldError("optionValue", ValidationCodes.NOT_APPLICABLE));
    }
    
    @Test
    public final void testDiscreteVariableNoOption()
    {
        final EditModelTerm editTerm = EditModelTerm.forVariable("wbc", 0.0045f);
        
        // Validation
        final Errors errors = validate(editTerm);
        assertOnlyTheseErrors(
                errors, new SimpleFieldError("optionValue", ValidationCodes.NO_VALUE));
    }
    
    @Test
    public final void testBuildNewRule() throws InvalidIdentifierException
    {
        final float coeff = 1.03f;
        final Rule rule = fModelService.getAllRules().asList().get(0);
        final DerivedTerm expected = new DerivedTerm(coeff, rule);
        
        // Basic properties
        final EditModelTerm editTerm = EditModelTerm.forRule(rule.getDisplayName(), coeff);
        assertEquals(TermType.RULE, editTerm.getTermType());
        assertEquals(rule.getDisplayName(), editTerm.getKey());
        assertEquals(coeff, editTerm.getCoefficient(), 0.0f);
        
        // Validation
        final Errors errors = validate(editTerm);
        assertEquals(ImmutableList.of(), errors.getAllErrors());
        
        // Build
        assertEquals(expected, editTerm.build(fModelService));
    }
    
    @Test
    public final void testRuleNoKey()
    {
        final EditModelTerm editTerm = new EditModelTerm();
        editTerm.setTermType(TermType.RULE);
        editTerm.setKey("");
        editTerm.setCoefficient(0.5f);
        
        // Validation
        final Errors errors = validate(editTerm);
        assertOnlyTheseErrors(
                errors, new SimpleFieldError("key", ValidationCodes.NO_VALUE));
    }
    
    @Test
    public final void testRuleWithOption()
    {
        final Rule rule = fModelService.getAllRules().asList().get(0);
        final EditModelTerm editTerm = EditModelTerm.forRule(rule.getDisplayName(), 0.4f);
        editTerm.setOptionValue("option");
        
        // Validation
        final Errors errors = validate(editTerm);
        assertOnlyTheseErrors(
                errors, new SimpleFieldError("optionValue", ValidationCodes.NOT_APPLICABLE));
    }
    
}

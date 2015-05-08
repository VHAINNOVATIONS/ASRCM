package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.AbstractVariableTest;
import gov.va.med.srcalc.domain.model.SampleModels;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public final class EditVariableValidatorTest
{
    /**
     * Instantiates a basic {@link EditVariable} instance.
     */
    private EditVariable makeEditVariable()
    {
        return new EditVariable(
                SampleModels.dnrVariable(), SampleModels.variableGroups());
    }
    
    /**
     * Validates the given object and returns the binding result.
     */
    private BeanPropertyBindingResult validate(final EditVariable ev)
    {
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(ev, "variable");
        new EditVariableValidator().validate(ev, errors);
        return errors;
    }
    
    @Test
    public final void testValid()
    {
        final BindingResult errors = validate(makeEditVariable());

        assertEquals("error count", 0, errors.getErrorCount());
    }

    @Test
    public final void testDisplayNameTooLong()
    {
        final EditVariable ev = makeEditVariable();
        ev.setDisplayName(AbstractVariableTest.EIGHTY_ONE_CHARS);
        final BindingResult errors = validate(ev);
        
        assertEquals(
                EditVariableValidator.ERROR_TOO_LONG,
                errors.getFieldError("displayName").getCode());
    }
    
    @Test
    public final void testDisplayNameInvalidCharacters()
    {
        final EditVariable ev = makeEditVariable();
        ev.setDisplayName("\t");
        final BindingResult errors = validate(ev);
        
        assertEquals(
                EditVariableValidator.ERROR_INVALID_CONTENTS,
                errors.getFieldError("displayName").getCode());
    }
    
    @Test
    public final void testInvalidGroup()
    {
        final EditVariable ev = makeEditVariable();
        ev.setGroupId(80);
        final BindingResult errors = validate(ev);
        
        assertEquals(
                EditVariableValidator.ERROR_INVALID_OPTION,
                errors.getFieldError("groupId").getCode());
    }
    
    @Test
    public final void testHelpTextTooLong()
    {
        final EditVariable ev = makeEditVariable();
        ev.setHelpText(AbstractVariableTest.FOUR_THOUSAND_ONE_CHARS);
        final BindingResult errors = validate(ev);
        
        assertEquals(
                EditVariableValidator.ERROR_TOO_LONG,
                errors.getFieldError("helpText").getCode());
    }
}

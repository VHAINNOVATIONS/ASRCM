package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.test.util.TestHelpers;
import gov.va.med.srcalc.util.ValidationCodes;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public final class EditBaseVarValidatorTest
{
    /**
     * Instantiates a basic {@link EditBaseVar} instance.
     */
    private EditBaseVar makeEditVar()
    {
        // Use EditBooleanVar as a basic implementation of EditVar.
        return new EditBooleanVar(
                SampleModels.dnrVariable(), new MockModelService());
    }
    
    /**
     * Validates the given object and returns the binding result.
     */
    private BeanPropertyBindingResult validate(final EditBaseVar ev)
    {
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(ev, "variable");
        // Use EditBaseVar.getValidator() to test that as well.
        ev.getValidator().validate(ev, errors);
        return errors;
    }
    
    @Test
    public final void testValid()
    {
        final BindingResult errors = validate(makeEditVar());

        assertEquals("error count", 0, errors.getErrorCount());
    }
    
    @Test
    public final void testKeyEmpty()
    {
        final EditBaseVar ev = makeEditVar();
        ev.setKey("");
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.NO_VALUE,
                errors.getFieldError("key").getCode());
    }
    
    @Test
    public final void testKeyTooLong()
    {
        final EditBaseVar ev = makeEditVar();
        ev.setKey(TestHelpers.stringOfLength(Variable.KEY_MAX + 1));
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_LONG,
                errors.getFieldError("key").getCode());
    }
    
    @Test
    public final void testKeyInvalidCharacters()
    {
        final EditBaseVar ev = makeEditVar();
        ev.setKey("foo!");
        final BindingResult errors = validate(ev);
        
        assertEquals("error count", 1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.INVALID_CONTENTS,
                errors.getFieldError("key").getCode());
    }
    
    @Test
    public final void testDisplayNameEmpty()
    {
        final EditBaseVar ev = makeEditVar();
        ev.setDisplayName("");
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.NO_VALUE,
                errors.getFieldError("displayName").getCode());
    }

    @Test
    public final void testDisplayNameTooLong()
    {
        final EditBaseVar ev = makeEditVar();
        ev.setDisplayName(TestHelpers.stringOfLength(Variable.DISPLAY_NAME_MAX + 1));
        final BindingResult errors = validate(ev);
        
        assertEquals("error count", 1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_LONG,
                errors.getFieldError("displayName").getCode());
    }
    
    @Test
    public final void testDisplayNameInvalidCharacters()
    {
        final EditBaseVar ev = makeEditVar();
        ev.setDisplayName("\t");
        final BindingResult errors = validate(ev);
        
        assertEquals("error count", 1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.INVALID_CONTENTS,
                errors.getFieldError("displayName").getCode());
    }
    
    @Test
    public final void testInvalidGroup()
    {
        final EditBaseVar ev = makeEditVar();
        ev.setGroupId(80);
        final BindingResult errors = validate(ev);
        
        assertEquals("error count", 1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.INVALID_OPTION,
                errors.getFieldError("groupId").getCode());
    }
    
    @Test
    public final void testHelpTextTooLong()
    {
        final EditBaseVar ev = makeEditVar();
        ev.setHelpText(TestHelpers.stringOfLength(Variable.HELP_TEXT_MAX + 1));
        final BindingResult errors = validate(ev);
        
        assertEquals("error count", 1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_LONG,
                errors.getFieldError("helpText").getCode());
    }
}

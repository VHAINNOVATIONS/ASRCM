package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;

import java.util.Arrays;

import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.domain.model.VariableGroup;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.test.util.TestHelpers;
import gov.va.med.srcalc.util.ValidationCodes;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class EditMultiSelectVarValidatorTest
{
    private static final Logger fLogger = LoggerFactory.getLogger(EditMultiSelectVarValidatorTest.class);

    private final MockModelService fModelService = new MockModelService();

    private EditMultiSelectVar makeEditVar()
    {
        final EditMultiSelectVar ev = new EditMultiSelectVar(
                fModelService);
        ev.setKey("validKey");
        ev.setDisplayName("validDisplayName");
        final VariableGroup group =
                fModelService.getAllVariableGroups().iterator().next();
        ev.setGroupId(group.getId());
        ev.setHelpText("validHelpText");
        ev.getOptions().clear();
        ev.getOptions().addAll(Arrays.asList(
                "validOption1",
                "validOption2",
                "validOption3",
                "validOption4",
                "validOption5",
                "validOption6",
                "validOption7",
                "validOption8",
                "validOption9",
                "validOption10",
                "validOption11",
                "validOption12",
                "validOption13",
                "validOption14",
                "validOption15",
                "validOption16",
                "validOption17",
                "validOption18",
                "validOption19",
                "validOption20",
                ""));   // Trailing blanks are omitted.
        return ev;
    }
    
    /**
     * Validates the given object and returns the binding result.
     */
    private BeanPropertyBindingResult validate(final EditMultiSelectVar ev)
    {
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(ev, "variable");
        // Use getValidator() to test that as well.
        final EditMultiSelectVarValidator validator = ev.getValidator();
        validator.validate(ev, errors);
        fLogger.debug("Errors are: {}", errors);
        return errors;
    }
    
    @Test
    public final void testValid()
    {
        final BindingResult errors = validate(makeEditVar());
        
        assertEquals("error count", 0, errors.getErrorCount());
    }
    
    @Test
    public final void testNullDisplayType()
    {
        final EditMultiSelectVar ev = makeEditVar();
        ev.setDisplayType(null);
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.NO_VALUE,
                errors.getFieldError("displayType").getCode());
    }
    
    @Test
    public final void testNoOptions()
    {
        final EditMultiSelectVar ev = makeEditVar();
        ev.getOptions().clear();
        ev.getOptions().add(""); // A blank is the same as none at all.

        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.NO_VALUE,
                errors.getFieldError("options").getCode());
    }
    
    @Test
    public final void testTooManyOptions()
    {
        final EditMultiSelectVar ev = makeEditVar();
        ev.getOptions().add(20, "extraOption");

        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_LONG,
                errors.getFieldError("options").getCode());
    }
    
    @Test
    public final void testOptionNoValue()
    {
        final EditMultiSelectVar ev = makeEditVar();
        ev.getOptions().remove(0);
        ev.getOptions().add(2, "");
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.NO_VALUE,
                errors.getFieldError("options[2]").getCode());
    }
    
    @Test
    public final void testOptionTooLong()
    {
        final EditMultiSelectVar ev = makeEditVar();
        // Remove any existing options for simplicity.
        ev.getOptions().clear();
        ev.getOptions().add(
                TestHelpers.stringOfLength(MultiSelectOption.VALUE_MAX + 1));
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_LONG,
                errors.getFieldError("options[0]").getCode());
    }
    
    @Test
    public final void testOptionInvalidChars()
    {
        final EditMultiSelectVar ev = makeEditVar();
        ev.getOptions().remove(0);
        ev.getOptions().add(0, "foo\t");
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.INVALID_CONTENTS,
                errors.getFieldError("options[0]").getCode());
    }
    
}

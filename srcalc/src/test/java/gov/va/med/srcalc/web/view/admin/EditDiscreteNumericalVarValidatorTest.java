package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.test.util.TestHelpers;
import gov.va.med.srcalc.util.ValidationCodes;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class EditDiscreteNumericalVarValidatorTest
{
    private static final Logger fLogger = LoggerFactory.getLogger(
            EditDiscreteNumericalVarValidatorTest.class);
    
    private final MockModelService fModelService = new MockModelService();
    
    /**
     * Returns a valid EditDiscreteNumericalVar instance.
     */
    private EditDiscreteNumericalVar makeEditVar()
    {
        final EditDiscreteNumericalVar ev = new EditDiscreteNumericalVar(fModelService);
        ev.setKey("validKey");
        ev.setDisplayName("validDisplayName");
        final VariableGroup group =
                fModelService.getAllVariableGroups().iterator().next();
        ev.setGroupId(group.getId());
        ev.setHelpText("validHelpText");
        ev.setUnits("valid/units");
        // Just leave the range at default, which is valid.
        // Populate the maximum (10) contiguous categories.
        ev.getCategories().clear();
        ev.getCategories().add(
                new CategoryBuilder().setValue("category1").setUpperBound(0.5f));
        ev.getCategories().add(
                new CategoryBuilder().setValue("category2").setUpperBound(10.0f));
        ev.getCategories().add(
                new CategoryBuilder().setValue("category3").setUpperBound(20.0f));
        ev.getCategories().add(
                new CategoryBuilder().setValue("category4").setUpperBound(30.0f));
        ev.getCategories().add(
                new CategoryBuilder().setValue("category5").setUpperBound(40.0f));
        ev.getCategories().add(
                new CategoryBuilder().setValue("category6").setUpperBound(50.0f));
        ev.getCategories().add(
                new CategoryBuilder().setValue("category7").setUpperBound(60.0f));
        ev.getCategories().add(
                new CategoryBuilder().setValue("category8").setUpperBound(70.0f));
        ev.getCategories().add(
                new CategoryBuilder().setValue("category9").setUpperBound(80.0f));
        ev.getCategories().add(
                new CategoryBuilder().setValue("category10").setUpperBound(100.0f));
        
        return ev;
    }
    
    private BeanPropertyBindingResult validate(final EditDiscreteNumericalVar ev)
    {
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(ev, "variable");
        // Use getValidator() to test that as well.
        final EditDiscreteNumericalVarValidator validator = ev.getValidator();
        assertTrue(validator.supports(ev.getClass()));
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
    public final void testUnitsTooLong()
    {
        final EditDiscreteNumericalVar ev = makeEditVar();
        ev.setUnits(TestHelpers.stringOfLength(DiscreteNumericalVariable.UNITS_MAX + 1));
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_LONG,
                errors.getFieldError("units").getCode());
        
    }
    
    @Test
    public final void testUnitsInvalidChars()
    {
        final EditDiscreteNumericalVar ev = makeEditVar();
        ev.setUnits("bar\t");
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.INVALID_CONTENTS,
                errors.getFieldError("units").getCode());
    }
    
    @Test
    public final void testTooFewCategories()
    {
        final EditDiscreteNumericalVar ev = makeEditVar();

        // First try with 0.
        ev.getCategories().clear();
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_SHORT,
                errors.getFieldError("categories").getCode());
        
        // Now try with 1.
        ev.getCategories().add(new CategoryBuilder().setValue("category1"));
        
        final BindingResult errors2 = validate(ev);
        
        assertEquals(1, errors2.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_SHORT,
                errors2.getFieldError("categories").getCode());
        
        // Now try with 2, but with the second one blank (which should be trimmed).
        ev.getCategories().add(new CategoryBuilder().setValue(""));
        
        final BindingResult errors3 = validate(ev);
        
        assertEquals(1, errors3.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_SHORT,
                errors3.getFieldError("categories").getCode());
    }
    
    @Test
    public final void testTooManyCategories()
    {
        final EditDiscreteNumericalVar ev = makeEditVar();
        ev.getCategories().add(new CategoryBuilder().setValue("category11"));
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_LONG,
                errors.getFieldError("categories").getCode());
    }
    
    @Test
    public final void testEmptyCategoryValue()
    {
        final EditDiscreteNumericalVar ev = makeEditVar();
        ev.getCategories().get(1).setValue("");
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.NO_VALUE,
                errors.getFieldError("categories[1].value").getCode());
    }

    @Test
    public final void testCategoryValueTooLong()
    {
        final EditDiscreteNumericalVar ev = makeEditVar();
        ev.getCategories().get(2).setValue(TestHelpers.stringOfLength(MultiSelectOption.VALUE_MAX + 1));
        
        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.TOO_LONG,
                errors.getFieldError("categories[2].value").getCode());
    }

    @Test
    public final void testCategoryValueInvalidCharacters()
    {
        final EditDiscreteNumericalVar ev = makeEditVar();
        ev.getCategories().get(0).setValue("foo\t");

        final BindingResult errors = validate(ev);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.INVALID_CONTENTS,
                errors.getFieldError("categories[0].value").getCode());
    }
}

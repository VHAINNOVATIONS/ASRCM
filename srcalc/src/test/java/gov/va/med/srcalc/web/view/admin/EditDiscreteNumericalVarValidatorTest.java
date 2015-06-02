package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;
import gov.va.med.srcalc.domain.model.VariableGroup;
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
    
}

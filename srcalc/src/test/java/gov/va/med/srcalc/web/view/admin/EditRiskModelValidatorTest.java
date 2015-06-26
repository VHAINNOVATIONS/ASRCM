package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.va.med.srcalc.util.ValidationCodes;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class EditRiskModelValidatorTest {

    /**
     * Validates the given object and returns the binding result.
     */
    private BeanPropertyBindingResult validate(final EditRiskModel erm)
    {
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(erm, "riskModel");
    	EditRiskModelValidator validator = new EditRiskModelValidator();

        assertTrue(validator.supports(erm.getClass()));
        
        validator.validate(erm, errors);
        
        return errors;
    }
	
    @Test
    public final void testEmptyName()
    {
    	final EditRiskModel erm = EditRiskModelTest.createEditRiskModel( "Thoracic 30-day Mortality" );
        erm.setModelName("");
        
        final BindingResult errors = validate(erm);
        
        assertEquals(1, errors.getErrorCount());
        assertEquals(
                ValidationCodes.NO_VALUE,
                errors.getFieldError("modelName").getCode());
    }
    
    @Test
    public final void testValid()
    {
        final BindingResult errors = validate(EditRiskModelTest.createEditRiskModel( "Thoracic 30-day Mortality" ) );
        
        assertEquals("error count", 0, errors.getErrorCount());
    }

}

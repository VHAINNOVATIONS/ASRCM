package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static gov.va.med.srcalc.test.util.TestHelpers.assertOnlyTheseErrors;
import gov.va.med.srcalc.domain.model.ModelTerm;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.test.util.SimpleFieldError;
import gov.va.med.srcalc.util.ValidationCodes;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class EditRiskModelValidatorTest 
{
    private final MockModelService fModelService = new MockModelService();
    
    /**
     * Validates the given object and returns the binding result.
     */
    private BeanPropertyBindingResult validate(final EditRiskModel erm)
    {
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(erm, "riskModel");
        EditRiskModelValidator validator = new EditRiskModelValidator(fModelService);

        assertTrue(validator.supports(erm.getClass()));
        
        validator.validate(erm, errors);
        
        return errors;
    }
    
    @Test
    public final void testEmptyName()
    {
        final EditRiskModel erm = EditRiskModel.fromRiskModel(SampleModels.thoracicRiskModel(), fModelService);
        erm.setModelName("");
        
        final BindingResult errors = validate(erm);
        
        assertOnlyTheseErrors(
                errors, new SimpleFieldError("modelName", ValidationCodes.NO_VALUE));
    }
    
    @Test
    public final void testDuplicateTerm()
    {
        /* Setup */
        final RiskModel sampleModel = SampleModels.thoracicRiskModel();
        // Pick an arbitrary term to duplicate.
        final ModelTerm sampleTerm = sampleModel.getTerms().asList().get(0);
        final EditRiskModel erm = EditRiskModel.fromRiskModel(sampleModel, fModelService);
        erm.getTerms().add(EditModelTerm.fromExistingTerm(sampleTerm));
        final int duplicateIndex = erm.getTerms().size() - 1;
        final SimpleFieldError expectedError = new SimpleFieldError(
                String.format("terms[%d]", duplicateIndex),
                ValidationCodes.DUPLICATE_VALUE);
        
        /* Behavior & Verification */
        final BindingResult errors = validate(erm);
        
        assertOnlyTheseErrors(
                errors, expectedError);
    }
    
    @Test
    public final void testValid()
    {
        final EditRiskModel erm = EditRiskModel.fromRiskModel(SampleModels.thoracicRiskModel(), fModelService);
        final BindingResult errors = validate(erm);
        
        assertEquals("error count", 0, errors.getErrorCount());
    }
}

package gov.va.med.srcalc.web.view.admin;

import java.util.HashSet;

import gov.va.med.srcalc.domain.model.ModelTerm;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EditRiskModelValidator implements Validator
{
    private final ModelInspectionService fModelService;
    
    public EditRiskModelValidator(final ModelInspectionService modelService)
    {
        fModelService = modelService;
    }
    
    /**
     * Returns true if, and only if, the given class is {@link EditRiskModel} or a subclass.
     */
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return EditRiskModel.class.isAssignableFrom(clazz);
    }

    /**
     * Validates the given object, using error codes from {@link ValidationCodes}.
     * @param obj the object to validate. Must be an instance of {@link EditRiskModel}.
     * @throws ClassCastException if the given object is not.
     */
    @Override
    public void validate(final Object obj, final Errors e)
    {
        // Note: the editRiskModel object is accessible from the e Error object.
        //
        ValidationUtils.rejectIfEmpty(e, "modelName", ValidationCodes.NO_VALUE);
        ValidationUtils2.rejectIfTooLong(e, "modelName", DisplayNameConditions.DISPLAY_NAME_MAX );
        
        ValidationUtils2.rejectIfDoesntMatch(
                e, "modelName", RiskModel.VALID_MODEL_NAME_PATTERN,
                new Object[] {RiskModel.VALID_MODEL_NAME_CHARACTERS});        
        
        /* Validate each EditModelTerm. */
        
        final EditRiskModel target = (EditRiskModel)obj;

        // Record a temporary version of the built terms for duplicate checking.
        final HashSet<ModelTerm> builtTerms = new HashSet<>(target.getTerms().size());

        for (int i = 0; i < target.getTerms().size(); ++i)
        {
            final EditModelTerm term = target.getTerms().get(i);
            final String nestedPath = String.format("terms[%d]", i);
            
            // Call the term's validator
            e.pushNestedPath(nestedPath);
            term.getValidator(fModelService).validate(term, e);
            e.popNestedPath();
            
            // If there are no validation errors, also check for duplicate terms.
            if (!e.hasErrors())
            {
                try
                {
                    if (!builtTerms.add(term.build(fModelService)))
                    {
                        e.rejectValue(
                                nestedPath, ValidationCodes.DUPLICATE_VALUE, "duplicate");
                    }
                }
                catch (final InvalidIdentifierException ex)
                {
                    throw new RuntimeException(
                            "Programming error: valid term threw Exception.", ex);
                }
            }
        }
    }
}

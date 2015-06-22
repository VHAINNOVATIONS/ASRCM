package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EditRiskModelValidator implements Validator {
    
    private static final Logger fLogger = LoggerFactory.getLogger(EditRiskModelValidator.class);

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
        final EditRiskModel editRMs = (EditRiskModel)obj;
        
//        fLogger.debug("Validating name of EditRiskModel {}", editRMs.getModelName() );

        // Note: the editMDs object is accessible from the e Error object.
        //
        ValidationUtils.rejectIfEmpty(e, "modelName", ValidationCodes.NO_VALUE);
        ValidationUtils2.rejectIfTooLong(e, "modelName", RiskModel.DISPLAY_NAME_MAX );
        
        ValidationUtils2.rejectIfDoesntMatch(
                e, "modelName", RiskModel.VALID_MODEL_NAME_PATTERN,
                new Object[] {RiskModel.VALID_MODEL_NAME_CHARACTERS});        

        fLogger.debug( " EditRiskModel validates" );
    }

}

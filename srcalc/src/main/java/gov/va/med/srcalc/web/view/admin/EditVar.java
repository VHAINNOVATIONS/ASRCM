package gov.va.med.srcalc.web.view.admin;

import org.springframework.validation.Validator;

/**
 * Basic interface for a class that edits a Variable.
 */
public interface EditVar
{
    /**
     * Returns a user-friendly name of the variable type.
     */
    public String getTypeName();
    
    /**
     * <p>Returns an appropriate Validator instance for validating this object.
     * </p>
     * 
     * <p>Implementations do not perform their own validation so that the web
     * code can directly bind user input to these beans without fear of
     * Exceptions. We instead provide a Spring Validator via this method to
     * safely ensure that user input meets the underlying variable class's
     * requirements.</p>
     */
    public abstract Validator getValidator();
}

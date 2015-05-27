package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.AbstractVariable;

/**
 * An interface for editing an existing {@link AbstractVariable}.
 */
public interface EditExistingVar
{
    /**
     * Returns the variable which we are editing.
     */
    public AbstractVariable getTargetVariable();
    
    /**
     * Returns the name of the View to edit the target variable.
     */
    public String getEditViewName();
    
    /**
     * Applies the queued changes to the target variable.
     * @return the target variable for convenience
     * @throws IllegalStateException if any queued changes are invalid
     */
    public AbstractVariable applyToVariable();
}

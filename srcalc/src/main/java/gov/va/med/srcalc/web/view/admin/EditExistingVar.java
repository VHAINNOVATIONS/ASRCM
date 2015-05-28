package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.AbstractVariable;

/**
 * <p>An interface for editing an existing {@link AbstractVariable}.</p>
 * 
 * <p>The high-level workflow is:</p>
 * 
 * <ol>
 * <li>Construct an instance based on an existing variable.</li>
 * <li>Present the user with the current values and allow her to update the
 * properties as desired.</li>
 * <li>Use the appropriate validator (e.g., {@link EditMultiSelectVarValidator})
 * to validate the user's edits.</li>
 * <li>Call {@link #applyToVariable()} to update the target variable with the
 * new properties.</li>
 * </ol>
 */
public interface EditExistingVar
{
    /**
     * Returns the variable which we are editing.
     */
    public AbstractVariable getTargetVariable();
    
    /**
     * Returns the name of the View to edit the target variable.
     * @return a non-empty String
     */
    public String getEditViewName();
    
    /**
     * Applies the queued changes to the target variable.
     * @return the target variable for convenience
     * @throws IllegalStateException if any queued changes are invalid
     */
    public AbstractVariable applyToVariable();
}

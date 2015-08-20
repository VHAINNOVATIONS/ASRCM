package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.model.Variable;


/**
 * Represents the given value of a {@link Variable}.
 */
public interface Value
{
    /**
     * Returns the variable to which this value belongs.
     */
    public Variable getVariable();
    
    /**
     * Returns an Object representing the actual value.
     */
    public Object getValue();
    
    /**
     * Returns a String representing the value but suitable for displaying to a
     * user.
     */
    public String getDisplayString();

    /**
     * Accepts the given {@link ValueVisitor}.
     */
    public void accept(ValueVisitor valueVisitor);
}

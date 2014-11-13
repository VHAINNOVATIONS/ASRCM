package gov.va.med.srcalc.domain.variable;

/**
 * Represents the given value of a {@link Variable}.
 */
public interface Value
{
    public Variable getVariable();
    
    /**
     * Returns an Object representing the actual value.
     */
    public Object getValue();
}

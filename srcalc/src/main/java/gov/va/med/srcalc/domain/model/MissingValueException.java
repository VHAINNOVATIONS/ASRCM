package gov.va.med.srcalc.domain.model;

/**
 * Indicates that there is a required variable that has no assigned value
 */
public class MissingValueException extends Exception
{
    private static final long serialVersionUID = 1L;

    private final Variable fVariable;
    
    public MissingValueException(final String message, final Variable variable)
    {
        super(message);
        fVariable = variable;
    }
    
    public Variable getVariable()
    {
        return fVariable;
    }
}

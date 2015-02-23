package gov.va.med.srcalc.domain.variable;

/**
 * Indicates that there is a required variable that has no assigned value
 */
public class MissingValueException extends Exception
{
	private static final long serialVersionUID = 1L;

	private final String fCode;
	private final Variable fVariable;
	
	public MissingValueException(final String message, final String code, final Variable variable)
	{
		super(message);
		fCode = code;
		fVariable = variable;
	}
	
	public String getCode()
	{
		return fCode;
	}
	
	public Variable getVariable()
	{
		return fVariable;
	}
}

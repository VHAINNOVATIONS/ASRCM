package gov.va.med.srcalc.util;

import java.util.List;

import gov.va.med.srcalc.domain.variable.MissingValueException;

/**
 * Indicates that there is at least one {@link MissingValueException}
 */
public class MissingValueListException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private final List<MissingValueException> fMissingValues;
	
	public MissingValueListException(final String message, final List<MissingValueException> missingValues)
	{
		super(message);
		fMissingValues = missingValues;
	}
	
	public List<MissingValueException> getMissingValues()
	{
		return fMissingValues;
	}
}

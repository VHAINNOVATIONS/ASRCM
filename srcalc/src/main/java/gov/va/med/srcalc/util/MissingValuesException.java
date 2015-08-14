package gov.va.med.srcalc.util;

import java.util.List;

import gov.va.med.srcalc.domain.model.MissingValueException;

/**
 * Indicates that there is at least one {@link MissingValueException}.
 */
public class MissingValuesException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    private final List<MissingValueException> fMissingValues;
    
    /**
     * Constructs an instance.
     * @param message the detail message for this exception
     * @param missingValues all of the {@link MissingValueException}s
     */
    public MissingValuesException(final String message, final List<MissingValueException> missingValues)
    {
        super(message);
        fMissingValues = missingValues;
    }
    
    /**
     * Returns all of the missing value exceptions.
     */
    public List<MissingValueException> getMissingValues()
    {
        return fMissingValues;
    }
}

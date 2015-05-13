package gov.va.med.srcalc.vista;

import java.util.List;
import java.util.Objects;

import com.google.common.base.Splitter;

/**
 * <p>Encapsulates the common VistA operation result pair of a machine-friendly
 * code and human-friendly status message. Immutable.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class VistaOperationResult
{
    private final String fCode;
    private final String fMessage;
    
    /**
     * Constructs an instance.
     * @param code the machine-readable code
     * @param message the human-readable message
     * @throws NullPointerException if either argument is null
     */
    public VistaOperationResult(final String code, final String message)
    {
        fCode = Objects.requireNonNull(code, "code must not be null");
        fMessage = Objects.requireNonNull(message, "message must not be null");
    }
    
    /**
     * Constructs an instance from the combined string of "code^message".
     * @see #toString()
     * @throws IllegalArgumentException if the String is not in the proper
     * format
     */
    public static VistaOperationResult fromString(final String combinedString)
    {
        // We never expect more than 2 parts, so enforce that limit.
        final int expectedParts = 2;
        final Splitter splitter = Splitter.on('^').limit(expectedParts);
        final List<String> parts = splitter.splitToList(combinedString);
        if (parts.size() != expectedParts)
        {
            throw new IllegalArgumentException(
                    "String not in expected format (code^message): " + combinedString);
        }

        return new VistaOperationResult(parts.get(0), parts.get(1));
    }

    /**
     * Returns the machine-readable code.
     */
    public String getCode()
    {
        return fCode;
    }

    /**
     * Returns the human-readable status.
     * @return
     */
    public String getMessage()
    {
        return fMessage;
    }
    
    /**
     * Returns the String representation "code^message".
     */
    @Override
    public String toString()
    {
        return String.format("%s^%s", fCode, fMessage);
    }
}

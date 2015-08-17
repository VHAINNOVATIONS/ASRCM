package gov.va.med.srcalc.domain.model;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * <p>Indicates that value(s) were required for one or more variables but were not available.</p>
 * 
 * <p>Note: even though this exception is intended to be thrown during calculations, it
 * is in the {@code model} package because various methods' contracts depend on it.</p>
 */
public class MissingValuesException extends Exception
{
    private static final long serialVersionUID = 2L;
    
    private final ImmutableSet<Variable> fMissingVariables;
    
    /**
     * Constructs an instance composed of the given MissingValueExceptions.
     * @param missingVariables See {@link #getMissingVariables()}. Must not be empty.
     * @throws IllegalArgumentException if the given set is empty
     */
    public MissingValuesException(
            final Set<Variable> missingVariables)
    {
        // Use a sentinel value for the superclass's message: we override getMessage()
        // below.
        super("unused message");
        fMissingVariables = ImmutableSet.copyOf(missingVariables);
        if (fMissingVariables.isEmpty())
        {
            throw new IllegalArgumentException("At least one missing variable is required.");
        }
    }
    
    /**
     * Returns a message in the format "Missing values for [variable keys]".
     */
    @Override
    public String getMessage()
    {
        return String.format("Missing values for %s", getVariableKeySet());
    }
    
    /**
     * Returns the variable key of each missing variable.
     */
    private ImmutableSet<String> getVariableKeySet()
    {
        final HashSet<String> keys = new HashSet<>(fMissingVariables.size());
        for (final Variable v : fMissingVariables)
        {
            keys.add(v.getKey());
        }
        return ImmutableSet.copyOf(keys);
    }
    
    /**
     * Returns the set of Variables for which values were missing. The set is Immutable,
     * though the variable definitions themselves are not.
     */
    public ImmutableSet<Variable> getMissingVariables()
    {
        return fMissingVariables;
    }
}

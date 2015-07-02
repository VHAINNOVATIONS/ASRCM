package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.Preconditions;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.*;

/**
 * <p>An option for a {@link MultiSelectVariable}. Simply wraps a String value,
 * but enforces constraints on the String.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Embeddable
public final class MultiSelectOption
{
    /**
     * The maximum length of a valid value: {@value}.
     * @see #getValue()
     */
    public static final int VALUE_MAX = 80;
    
    /**
     * A regular expression that defines a valid value. Same as {@link
     * Variable#VALID_DISPLAY_NAME_REGEX}.
     */
    public static final String VALID_VALUE_REGEX = DisplayNameConditions.VALID_DISPLAY_NAME_REGEX;
    
    /**
     * English description of the valid value characters for readable error
     * messages.
     * @see #VALID_VALUE_REGEX
     */
    public static final String VALID_VALUE_CHARACTERS = DisplayNameConditions.VALID_DISPLAY_NAME_CHARACTERS;
    
    /**
     * Precompiled version of {@link #VALID_VALUE_REGEX} for efficiency.
     */
    public static final Pattern VALID_VALUE_PATTERN = Pattern.compile(VALID_VALUE_REGEX);
    
    private String fValue;
    
    /**
     * Constructs an instance with dummy values for the basic properties.
     * Intended for bean construction only.
     */
    MultiSelectOption()
    {
        fValue = "unset";
    }
    
    /**
     * Creates an instance with the given value.
     * @throws NullPointerException if the given value is null
     * @throws IllegalArgumentException if the given value is empty, over
     * {@link #VALUE_MAX} characters, or does not match {@link #VALID_VALUE_REGEX}.
     * @param value
     */
    public MultiSelectOption(final String value)
    {
        // Use setter to enforce constraints.
        setValue(value);
    }
    
    /**
     * The option value. E.g., "Male" for gender.
     */
    @Basic
    @Column(
            name = "OPTION_VALUE",  // "VALUE" is a SQL reserved word
            nullable = false,
            length = VALUE_MAX)
    public String getValue()
    {
        return fValue;
    }
    
    /**
     * For reflection-based construction only. To the outside world, the value is
     * immutable.
     * @throws IllegalArgumentException if the given value is invalid. (See
     * {@link #MultiSelectOption(String)}).
     */
    void setValue(final String value)
    {
        // Check preconditions
        Preconditions.requireWithin(value, 1, VALUE_MAX); // require at least 1 char
        Preconditions.requireMatches(value, "value", VALID_VALUE_PATTERN);

        fValue = value;
    }
    
    @Override
    public String toString()
    {
        return getValue();
    }
    
    /**
     * Checks value equality based on {@link #getValue()}.
     */
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof MultiSelectOption)
        {
            final MultiSelectOption other = (MultiSelectOption)o;
            return Objects.equals(this.getValue(), other.getValue());
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(getValue());
    }
}

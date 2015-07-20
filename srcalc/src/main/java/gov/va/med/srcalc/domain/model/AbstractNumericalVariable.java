package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.Preconditions;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.*;

/**
 * Implements base properties (valid range and units) for Variable that has a numerical
 * value.
 */
@MappedSuperclass
public abstract class AbstractNumericalVariable extends AbstractVariable
{
    /**
     * The maximum length of the units string.
     */
    public static final int UNITS_MAX = 40;
    
    /**
     * A regular expression that defines a valid units string. Same as
     * {@link DisplayNameConditions#VALID_DISPLAY_NAME_REGEX}.
     */
    public static final String VALID_UNITS_REGEX = DisplayNameConditions.VALID_DISPLAY_NAME_REGEX;
    
    /**
     * English description of the valid units characters for readable error
     * messages.
     * @see #VALID_UNITS_REGEX
     */
    public static final String VALID_UNITS_CHARACTERS = DisplayNameConditions.VALID_DISPLAY_NAME_CHARACTERS;
    
    /**
     * Precompiled version of {@link #VALID_UNITS_REGEX} for efficiency.
     */
    public static final Pattern VALID_UNITS_PATTERN = Pattern.compile(VALID_UNITS_REGEX);
    
    private NumericalRange fRange = new NumericalRange(0.0f, true, 100.0f, true);

    private String fUnits = "";

    /**
     * For reflection-based construction only. Business code should use
     * {@link #NumericalVariable(String, VariableGroup)}.
     */
    AbstractNumericalVariable()
    {
    }
    
    /**
     * Constructs an instance.
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if any argument is invalid
     * @see AbstractVariable#AbstractVariable(String, VariableGroup, String)
     */
    public AbstractNumericalVariable(final String displayName, final VariableGroup group, final String key)
    {
        super(displayName, group, key);
    }
    
    /**
     * Returns the range of valid values for the variable. The default is
     * [0.0,100.0].
     */
    @Embedded
    public NumericalRange getValidRange()
    {
        return fRange;
    }
    
    /**
     * Sets the range of valid values for the variable.
     * @throws NullPointerException if the given range is null
     */
    public void setValidRange(final NumericalRange range)
    {
        fRange = Objects.requireNonNull(range);
    }
    
    /**
     * The units (if any) for the number. May be an empty string if units are
     * not applicable, but will never be null.
     */
    @Basic
    @Column(nullable = false, length = UNITS_MAX)
    public String getUnits()
    {
        return fUnits;
    }

    /**
     * Sets the units.
     * @param units must not be null
     * @throws NullPointerException if the given value is null
     * @throws IllegalArgumentException if the given value is over 40 characters
     */
    public void setUnits(final String units)
    {
        // Check preconditions
        Preconditions.requireWithin(units, 0, UNITS_MAX);
        Preconditions.requireMatches(units, "units", VALID_UNITS_PATTERN);

        fUnits = units;
    }
}

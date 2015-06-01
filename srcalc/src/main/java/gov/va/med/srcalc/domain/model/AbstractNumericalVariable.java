package gov.va.med.srcalc.domain.model;

import java.util.Objects;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractNumericalVariable extends AbstractVariable
{
    /**
     * The maximum length of the units string.
     */
    public static final int UNITS_MAX = 40;
    
    private NumericalRange fRange = new NumericalRange(0.0f, true, 200.0f, true);

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
     * [0.0,200.0].
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
        // Note: will throw an NPE if the argument is null
        if (units.length() > UNITS_MAX)
        {
            throw new IllegalArgumentException("The units must be 40 characters or less.");
        }
        fUnits = units;
    }
}

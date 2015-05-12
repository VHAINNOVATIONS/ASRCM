package gov.va.med.srcalc.domain.model;

// TODO: can we eliminate this dependency on the 'calculation' package?
import gov.va.med.srcalc.domain.calculation.MultiSelectValue;

import java.util.*;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

@Entity
public class MultiSelectVariable extends AbstractVariable implements DiscreteVariable
{
    public enum DisplayType
    {
        Radio,
        Dropdown
    }
    
    private DisplayType fDisplayType;
    
    private List<MultiSelectOption> fOptions;
    
    /**
     * For reflections-based construction only. Business code should use
     * {@link #MultiSelectVariable(String, VariableGroup, DisplayType, List)}.
     */
    MultiSelectVariable()
    {
        fDisplayType = DisplayType.Radio;
        fOptions = new ArrayList<>();
    }
    
    /**
     * Constructs an instance.
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if any argument is invalid
     * @see AbstractVariable#AbstractVariable(String, VariableGroup, String)
     * @see #setDisplayType(DisplayType)
     * @see #setOptions(List)
     */
    public MultiSelectVariable(
            final String displayName,
            final VariableGroup group,
            final DisplayType displayType,
            final List<MultiSelectOption> options,
            final String key)
    {
        super(displayName, group, key);
        setDisplayType(displayType);
        setOptions(options);
    }
    
    @Basic
    @Enumerated(EnumType.STRING)  // store as strings in the DB for user-friendliness
    public final DisplayType getDisplayType()
    {
        return fDisplayType;
    }
    
    /**
     * Sets the display type of the variable.
     * @throws NullPointerException if the given value is null. (Yes, you can
     * pass a null value for an enum.)
     */
    public final void setDisplayType(final DisplayType displayType)
    {
        fDisplayType = Objects.requireNonNull(displayType, "display type must not be null");
    }

    @OneToMany(fetch = FetchType.EAGER)  // eager load due to close association
    @OrderColumn(name = "option_index")
    @JoinTable(
            name = "multi_select_variable_option",
            joinColumns = @JoinColumn(name = "variable_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
        )
    public final List<MultiSelectOption> getOptions()
    {
        return fOptions;
    }

    /**
     * Sets the ordered list of {@link MultiSelectOption}s.
     * @param options
     */
    public final void setOptions(final List<MultiSelectOption> options)
    {
        fOptions = Objects.requireNonNull(options, "options must not be null");
    }
    
    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitMultiSelect(this);
    }
    
    public MultiSelectValue makeValue(final MultiSelectOption selectedOption)
    {
        return new MultiSelectValue(this, selectedOption);
    }
}

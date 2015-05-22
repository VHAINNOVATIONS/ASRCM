package gov.va.med.srcalc.domain.model;

// TODO: can we eliminate this dependency on the 'calculation' package?
import gov.va.med.srcalc.domain.calculation.MultiSelectValue;

import java.util.*;

import javax.persistence.*;

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
     * @param options the ordered list of options. This constructor will make a
     * defensive copy of the list.
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
        fOptions = new ArrayList<>(options);
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

    /**
     * Returns the ordered list of {@link MultiSelectOption}s.
     * @return a modifiable list
     */
    @ElementCollection(fetch = FetchType.EAGER)  // eager load due to close association
    @OrderColumn(name = "option_index")
    // Override strange defaults
    @CollectionTable(
            name = "multi_select_variable_option",
            joinColumns = @JoinColumn(name = "variable_id"))
    public final List<MultiSelectOption> getOptions()
    {
        return fOptions;
    }

    /**
     * <p>Sets the ordered list of {@link MultiSelectOption}s.</p>
     * 
     * <p>This method is for bean construction only. To modify the collection of
     * an existing object, modify the list returned by {@link
     * #getOptions()}.</p>
     */
    final void setOptions(final List<MultiSelectOption> options)
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

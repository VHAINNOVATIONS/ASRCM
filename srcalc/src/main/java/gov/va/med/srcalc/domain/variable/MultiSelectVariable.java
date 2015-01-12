package gov.va.med.srcalc.domain.variable;

import java.util.List;

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
    }
    
    public MultiSelectVariable(
            final String displayName,
            final VariableGroup group,
            final DisplayType displayType,
            final List<MultiSelectOption> options)
    {
        super(displayName, group);
        fDisplayType = displayType;
        fOptions = options;
    }
    
    @Basic
    @Enumerated(EnumType.STRING)  // store as strings in the DB for user-friendliness
    public DisplayType getDisplayType()
    {
        return fDisplayType;
    }
    
    public void setDisplayType(final DisplayType displayType)
    {
        fDisplayType = displayType;
    }

    @OneToMany(fetch = FetchType.EAGER)  // eager load due to close association
    @OrderColumn(name = "option_index")
    @JoinTable(
            name = "multi_select_variable_option",
            joinColumns = @JoinColumn(name = "variable_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
        )
    public List<MultiSelectOption> getOptions()
    {
        return fOptions;
    }

    public void setOptions(final List<MultiSelectOption> options)
    {
        fOptions = options;
    }
    
    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitMultiSelect(this);
    }
}

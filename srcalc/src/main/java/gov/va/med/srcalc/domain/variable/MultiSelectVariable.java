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
public class MultiSelectVariable extends Variable
{
    public enum DisplayType
    {
        Radio,
        Dropdown
    }
    
    private DisplayType fDisplayType;
    
    private List<MultiSelectOption> fOptions;
    
    public MultiSelectVariable()
    {
    }
    
    public MultiSelectVariable(
            String displayName, DisplayType displayType, List<MultiSelectOption> options)
    {
        super(displayName);
        fDisplayType = displayType;
        fOptions = options;
    }
    
    @Basic
    @Enumerated(EnumType.STRING)  // store as strings in the DB for user-friendliness
    public DisplayType getDisplayType()
    {
        return fDisplayType;
    }
    
    public void setDisplayType(DisplayType displayType)
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

    public void setOptions(List<MultiSelectOption> options)
    {
        fOptions = options;
    }
    
    @Override
    public void accept(VariableVisitor visitor) throws Exception
    {
        visitor.visitMultiSelect(this);
    }
}

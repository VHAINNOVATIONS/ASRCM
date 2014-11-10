package gov.va.med.srcalc.domain.variable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * An option for a {@link MultiSelectVariable}.
 */
@Entity
public class MultiSelectOption
{
    private int fId;
    private String fValue;
    
    public MultiSelectOption()
    {
    }
    
    public MultiSelectOption(String value)
    {
        fValue = value;
    }
    
    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id
    public int getId()
    {
        return fId;
    }

    /**
     * For reflection-based construction only. Business code should never modify
     * the surrogate key as it is generated from the database.
     */
    void setId(int id)
    {
        this.fId = id;
    }
    
    /**
     * The option value. E.g., "Male" for gender.
     */
    @Basic
    public String getValue()
    {
        return fValue;
    }
    
    /**
     * For reflection-based construction only. To the outside world, the value is
     * immutable.
     */
    void setValue(String value)
    {
        fValue = value;
    }
    
    @Override
    public String toString()
    {
        return getValue();
    }
}

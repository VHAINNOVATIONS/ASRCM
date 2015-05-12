package gov.va.med.srcalc.domain.model;

import java.util.Objects;
import javax.persistence.*;

/**
 * <p>An option for a {@link MultiSelectVariable}.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
public final class MultiSelectOption
{
    private int fId;
    private String fValue;
    
    public MultiSelectOption()
    {
    }
    
    public MultiSelectOption(final String value)
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
    void setId(final int id)
    {
        this.fId = id;
    }
    
    /**
     * The option value. E.g., "Male" for gender.
     */
    @Basic
    @Column(name = "OPTION_VALUE")  // "VALUE" is a SQL reserved word
    public String getValue()
    {
        return fValue;
    }
    
    /**
     * For reflection-based construction only. To the outside world, the value is
     * immutable.
     */
    void setValue(final String value)
    {
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

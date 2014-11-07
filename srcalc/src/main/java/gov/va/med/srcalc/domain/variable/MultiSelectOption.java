package gov.va.med.srcalc.domain.variable;

/**
 * An option for a {@link MultiSelectVariable}.
 */
public class MultiSelectOption
{
    private String fValue;
    
    public MultiSelectOption()
    {
    }
    
    public MultiSelectOption(String value)
    {
        fValue = value;
    }
    
    /**
     * The option value. E.g., "Male" for gender.
     */
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

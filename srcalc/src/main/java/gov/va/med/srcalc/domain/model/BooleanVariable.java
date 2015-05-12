package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.BooleanValue;
import gov.va.med.srcalc.domain.calculation.Value;

import javax.persistence.Entity;

/**
 * A Variable with Boolean (yes/no) value.
 */
@Entity
public class BooleanVariable extends AbstractVariable
{
    /**
     * For reflection-based construction only. Business code should use
     * {@link #BooleanVariable(String, VariableGroup)}.
     */
    BooleanVariable()
    {
    }
    
    /**
     * Constructs an instance.
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if any argument is invalid
     * @see AbstractVariable#AbstractVariable(String, VariableGroup, String)
     */
    public BooleanVariable(final String displayName, final VariableGroup group, final String key)
    {
        super(displayName, group, key);
    }
    
    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitBoolean(this);
    }
    
    /**
     * Returns a {@link Value} object representing the given boolean value.
     * @param boolValue
     */
    public BooleanValue makeValue(final boolean boolValue)
    {
        return new BooleanValue(this, boolValue);
    }
}

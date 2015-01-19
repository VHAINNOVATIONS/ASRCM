package gov.va.med.srcalc.domain.variable;

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
     */
    public BooleanVariable(final String displayName, final VariableGroup group)
    {
        super(displayName, group);
    }
    
    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitBoolean(this);
    }
    
    /**
     * Returns a {@link Value} object representing the given boolean value.
     * @param boolValue
     * @return
     */
    public BooleanValue makeValue(final boolean boolValue)
    {
        return new BooleanValue(this, boolValue);
    }
}

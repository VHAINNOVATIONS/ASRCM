package gov.va.med.srcalc.domain.variable;

import javax.persistence.Entity;

/**
 * A Variable with Boolean (yes/no) value.
 */
@Entity
public class BooleanVariable extends Variable
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
    
}

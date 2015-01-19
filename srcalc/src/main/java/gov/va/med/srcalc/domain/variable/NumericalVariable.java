package gov.va.med.srcalc.domain.variable;

import javax.persistence.*;

/**
 * A Variable with numerical (floating-point) value.
 */
@Entity
public class NumericalVariable extends AbstractNumericalVariable
{
    /**
     * For reflection-based construction only. Business code should use
     * {@link #NumericalVariable(String, VariableGroup)}.
     */
    NumericalVariable()
    {
    }
    
    public NumericalVariable(final String displayName, final VariableGroup group)
    {
        super(displayName, group);
    }
    
    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitNumerical(this);
    }
    
    /**
     * Returns a {@link Value} object representing the given numerical value.
     * @param floatValue
     * @return
     * @throws ValueTooLowException if the value is below the minimum
     * @throws ValueTooHighException if the value is above the minimum
     */
    public NumericalValue makeValue(final float floatValue)
            throws ValueTooLowException, ValueTooHighException
    {
        return new NumericalValue(this, floatValue);
    }
}

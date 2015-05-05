package gov.va.med.srcalc.domain.model;

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
    
    /**
     * Constructs an instance.
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if any argument is invalid
     * @see AbstractVariable#AbstractVariable(String, VariableGroup, String)
     */
    public NumericalVariable(
            final String displayName, final VariableGroup group, final String key)
    {
        super(displayName, group, key);
    }
    
    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitNumerical(this);
    }
    
    /**
     * Returns a {@link Value} object representing the given numerical value.
     * @param floatValue
     * @throws ValueTooLowException if the value is below the minimum
     * @throws ValueTooHighException if the value is above the minimum
     */
    public NumericalValue makeValue(final float floatValue)
            throws ValueTooLowException, ValueTooHighException
    {
        return new NumericalValue(this, floatValue);
    }
}

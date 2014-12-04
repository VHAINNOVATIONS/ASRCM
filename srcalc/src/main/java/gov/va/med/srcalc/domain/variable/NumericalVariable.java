package gov.va.med.srcalc.domain.variable;

import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 * A Variable with numerical (floating-point) value.
 */
@Entity
public class NumericalVariable extends Variable
{
    private float fMinValue = 0.0f;
    private float fMaxValue = Float.POSITIVE_INFINITY;
    
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

    /**
     * The minimum allowed value for this variable. Default 0.
     */
    @Basic
    public float getMinValue()
    {
        return fMinValue;
    }

    public void setMinValue(final float minValue)
    {
        this.fMinValue = minValue;
    }

    /**
     * The maximum allowed value for this variable. Default
     * {@link Float#POSITIVE_INFINITY}.
     */
    @Basic
    public float getMaxValue()
    {
        return fMaxValue;
    }

    public void setMaxValue(final float maxValue)
    {
        this.fMaxValue = maxValue;
    }
    
    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitNumerical(this);
    }
}

package gov.va.med.srcalc.domain.variable;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class NumericalVariable extends Variable
{
    private int fMinValue = 0;
    private int fMaxValue = Integer.MAX_VALUE;
    
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
    public int getMinValue()
    {
        return fMinValue;
    }

    public void setMinValue(final int minValue)
    {
        this.fMinValue = minValue;
    }

    /**
     * The maximum allowed value for this variable. Default {@link Integer#MAX_VALUE}
     * @return
     */
    @Basic
    public int getMaxValue()
    {
        return fMaxValue;
    }

    public void setMaxValue(final int maxValue)
    {
        this.fMaxValue = maxValue;
    }
    
    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitNumerical(this);
    }
}

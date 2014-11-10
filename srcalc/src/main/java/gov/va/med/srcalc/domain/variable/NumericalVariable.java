package gov.va.med.srcalc.domain.variable;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class NumericalVariable extends Variable
{
    private int fMinValue = 0;
    private int fMaxValue = Integer.MAX_VALUE;
    
    public NumericalVariable()
    {
    }
    
    public NumericalVariable(String displayName)
    {
        super(displayName);
    }

    /**
     * The minimum allowed value for this variable. Default 0.
     */
    @Basic
    public int getMinValue()
    {
        return fMinValue;
    }

    public void setMinValue(int minValue)
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

    public void setMaxValue(int maxValue)
    {
        this.fMaxValue = maxValue;
    }
    
    @Override
    public void accept(VariableVisitor visitor) throws Exception
    {
        visitor.visitNumerical(this);
    }
}

package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.model.*;

/**
 * A value of a {@link ProcedureVariable}.
 */
public class ProcedureValue implements Value
{
    private final ProcedureVariable fVariable;
    private final Procedure fSelectedProcedure;
    
    /**
     * Constructs an instance from the selected procedure.
     * @param variable
     * @param selectedProcedure
     */
    public ProcedureValue(final ProcedureVariable variable, final Procedure selectedProcedure)
    {
        fVariable = variable;
        fSelectedProcedure = selectedProcedure;
    }
    
    @Override
    public ProcedureVariable getVariable()
    {
        return fVariable;
    }
    
    /**
     * Returns the selected Procedure.
     */
    @Override
    public Procedure getValue()
    {
        return fSelectedProcedure;
    }
    
    @Override
    public String getDisplayString()
    {
        // Return the long string for results display.
        return getValue().getLongString();
    }
    
    @Override
    public void accept(final ValueVisitor visitor)
    {
        visitor.visitProcedure(this);
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %s", getVariable(), getValue());
    }
}

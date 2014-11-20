package gov.va.med.srcalc.domain.variable;

import gov.va.med.srcalc.domain.Procedure;

public class ProcedureValue implements Value
{
    private final ProcedureVariable fVariable;
    private final Procedure fSelectedOption;
    
    public ProcedureValue(final ProcedureVariable variable, final Procedure selectedOption)
    {
        fVariable = variable;
        fSelectedOption = selectedOption;
    }
    
    @Override
    public ProcedureVariable getVariable()
    {
        return fVariable;
    }
    
    public Procedure getSelectedProcedure()
    {
        return fSelectedOption;
    }
    
    @Override
    public String getValue()
    {
        // Return the long string for results display.
        return getSelectedProcedure().getLongString();
    }
    
    @Override
    public String toString()
    {
        return String.format("%s = %s", getVariable(), getSelectedProcedure());
    }
}

package gov.va.med.srcalc.domain.model;


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
    
    /**
     * Returns the selected Procedure.
     */
    @Override
    public Procedure getValue()
    {
        return fSelectedOption;
    }
    
    @Override
    public String getDisplayString()
    {
        // Return the long string for results display.
        return getValue().getLongString();
    }
    
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

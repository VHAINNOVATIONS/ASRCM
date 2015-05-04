package gov.va.med.srcalc.domain.model;


public interface ValueVisitor
{
	public void visitNumerical(NumericalValue value);
    public void visitBoolean(BooleanValue value);
    public void visitMultiSelect(MultiSelectValue value);
    public void visitProcedure(ProcedureValue value);
    public void visitDiscreteNumerical(DiscreteNumericalValue value);
    
}

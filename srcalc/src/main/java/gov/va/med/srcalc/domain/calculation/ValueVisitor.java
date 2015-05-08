package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.calculation.*;


public interface ValueVisitor
{
	public void visitNumerical(NumericalValue value);
    public void visitBoolean(BooleanValue value);
    public void visitMultiSelect(MultiSelectValue value);
    public void visitProcedure(ProcedureValue value);
    public void visitDiscreteNumerical(DiscreteNumericalValue value);
    
}

package gov.va.med.srcalc.web;

import gov.va.med.srcalc.domain.variable.BooleanValue;
import gov.va.med.srcalc.domain.variable.DiscreteNumericalValue;
import gov.va.med.srcalc.domain.variable.MultiSelectValue;
import gov.va.med.srcalc.domain.variable.NumericalValue;
import gov.va.med.srcalc.domain.variable.ProcedureValue;
import gov.va.med.srcalc.domain.variable.Value;

public abstract class ExceptionlessValueVisitor implements ValueVisitor
{
	@Override
	public abstract void visitNumerical(NumericalValue value);

	@Override
	public abstract void visitBoolean(BooleanValue value);

	@Override
	public abstract void visitMultiSelect(MultiSelectValue value);

	@Override
	public abstract void visitProcedure(ProcedureValue value);

	@Override
	public abstract void visitDiscreteNumerical(DiscreteNumericalValue value);
	
	public void visit(final Value visitor)
    {
    	visitor.accept(this);
    }
}

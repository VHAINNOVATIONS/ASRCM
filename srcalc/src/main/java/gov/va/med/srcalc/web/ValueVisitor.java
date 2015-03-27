package gov.va.med.srcalc.web;

import gov.va.med.srcalc.domain.variable.BooleanValue;
import gov.va.med.srcalc.domain.variable.DiscreteNumericalValue;
import gov.va.med.srcalc.domain.variable.MultiSelectValue;
import gov.va.med.srcalc.domain.variable.NumericalValue;
import gov.va.med.srcalc.domain.variable.ProcedureValue;

public interface ValueVisitor
{
	public void visitNumerical(NumericalValue value);
    public void visitBoolean(BooleanValue value);
    public void visitMultiSelect(MultiSelectValue value);
    public void visitProcedure(ProcedureValue value);
    public void visitDiscreteNumerical(DiscreteNumericalValue value);
    
}

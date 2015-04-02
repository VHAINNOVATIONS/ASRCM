package gov.va.med.srcalc.domain.variable;


/**
 * <p>Base class for a {@link ValueVisitor} that doesn't throw any exceptions.
 * </p>
 * 
 * <p>Exists mainly for the convenient {@link #visit(Value)}.</p>
 */
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

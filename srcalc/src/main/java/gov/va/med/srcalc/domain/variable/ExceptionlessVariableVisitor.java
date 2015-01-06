package gov.va.med.srcalc.domain.variable;

/**
 * <p>Base class for a {@link VariableVisitor} that doesn't throw any exceptions.
 * </p>
 * 
 * <p>Exists mainly for the convenient {@link #visit(Variable)}.</p>
 */
public abstract class ExceptionlessVariableVisitor implements VariableVisitor
{
    @Override
    public abstract void visitNumerical(NumericalVariable variable);

    @Override
    public abstract void visitBoolean(BooleanVariable variable);

    @Override
    public abstract void visitMultiSelect(MultiSelectVariable variable);

    @Override
    public abstract void visitProcedure(ProcedureVariable variable);

    @Override
    public abstract void visitLab(LabVariable variable);
    
    /**
     * Convenience method to call {@link Variable#accept(VariableVisitor)}
     * without worrying about checked exceptions.
     */
    public void visit(final Variable variable)
    {
        try
        {
            variable.accept(this);
        }
        catch (Exception e)
        {
            // If it threw any exception, we know it's a RuntimeException.
            throw new RuntimeException(
                    e.getMessage(), e);
        }
    }
}

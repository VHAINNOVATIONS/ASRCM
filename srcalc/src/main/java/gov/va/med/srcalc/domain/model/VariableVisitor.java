package gov.va.med.srcalc.domain.model;

/**
 * Gang-of-Four Visitor pattern.
 */
public interface VariableVisitor
{
    public void visitNumerical(NumericalVariable variable) throws Exception;
    public void visitBoolean(BooleanVariable variable) throws Exception;
    public void visitMultiSelect(MultiSelectVariable variable) throws Exception;
    public void visitProcedure(ProcedureVariable variable) throws Exception;
    public void visitDiscreteNumerical(DiscreteNumericalVariable variable) throws Exception;
}

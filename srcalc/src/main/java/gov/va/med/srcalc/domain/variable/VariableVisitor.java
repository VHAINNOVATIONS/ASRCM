package gov.va.med.srcalc.domain.variable;

/**
 * Gang-of-Four Visitor pattern.
 */
public interface VariableVisitor
{
    public void visitNumerical(NumericalVariable variable) throws Exception;
    public void visitBoolean(BooleanVariable variable) throws Exception;
    public void visitMultiSelect(MultiSelectVariable variable) throws Exception;
    public void visitProcedure(ProcedureVariable variable) throws Exception;
    public void visitLab(LabVariable variable) throws Exception;
}

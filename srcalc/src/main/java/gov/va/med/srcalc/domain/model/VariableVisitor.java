package gov.va.med.srcalc.domain.model;

/**
 * Gang-of-Four Visitor pattern.
 */
public interface VariableVisitor
{
    /**
     * The visitor method for the {@link NumericalVariable} type.
     * @param variable
     * @throws Exception if there is a problem visiting the variable
     */
    public void visitNumerical(NumericalVariable variable) throws Exception;
    /**
     * The visitor method for the {@link BooleanVariable} type.
     * @param variable
     * @throws Exception if there is a problem visiting the variable
     */
    public void visitBoolean(BooleanVariable variable) throws Exception;
    /**
     * The visitor method for the {@link MultiSelectVariable} type.
     * @param variable
     * @throws Exception if there is a problem visiting the variable
     */
    public void visitMultiSelect(MultiSelectVariable variable) throws Exception;
    /**
     * The visitor method for the {@link ProcedureVariable} type.
     * @param variable
     * @throws Exception if there is a problem visiting the variable
     */
    public void visitProcedure(ProcedureVariable variable) throws Exception;
    /**
     * The visitor method for the {@link DiscreteNumericalVariable} type.
     * @param variable
     * @throws Exception if there is a problem visiting the variable
     */
    public void visitDiscreteNumerical(DiscreteNumericalVariable variable) throws Exception;
}

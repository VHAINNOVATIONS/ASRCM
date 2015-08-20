package gov.va.med.srcalc.domain.calculation;

/**
 * Gang-of-four Visitor pattern over the {@link Value} type hierarchy.
 */
public interface ValueVisitor
{
    /**
     * The visitor method for the {@link NumericalValue} type.
     * @param value
     */
    public void visitNumerical(NumericalValue value);
    /**
     * The visitor method for the {@link BooleanValue} type.
     * @param value
     */
    public void visitBoolean(BooleanValue value);
    /**
     * The visitor method for the {@link MultiSelectValue} type.
     * @param value
     */
    public void visitMultiSelect(MultiSelectValue value);
    /**
     * The visitor method for the {@link ProcedureValue} type.
     * @param value
     */
    public void visitProcedure(ProcedureValue value);
    /**
     * The visitor method for the {@link DiscreteNumericalValue} type.
     * @param value
     */
    public void visitDiscreteNumerical(DiscreteNumericalValue value);
    
}

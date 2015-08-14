package gov.va.med.srcalc.domain.model;

/**
 * Gang-of-Four Visitor pattern for {@link ModelTerm}s. Lets other classes
 * define new operations on the ModelTerm class hierarchy without modifying it.
 */
public interface ModelTermVisitor
{
    /**
     * The visitor method for the {@link ConstantTerm} type.
     * @param term
     */
    public void visitConstantTerm(final ConstantTerm term);
    /**
     * The visitor method for the {@link DerivedTerm} type.
     * @param term
     */
    public void visitDerivedTerm(final DerivedTerm term);
    /**
     * The visitor method for the {@link BooleanTerm} type.
     * @param term
     */
    public void visitBooleanTerm(final BooleanTerm term);
    /**
     * The visitor method for the {@link DiscreteTerm} type.
     * @param term
     */
    public void visitDiscreteTerm(final DiscreteTerm term);
    /**
     * The visitor method for the {@link NumericalTerm} type.
     * @param term
     */
    public void visitNumericalTerm(final NumericalTerm term);
    /**
     * The visitor method for the {@link ProcedureTerm} type.
     * @param term
     */
    public void visitProcedureTerm(final ProcedureTerm term);
}

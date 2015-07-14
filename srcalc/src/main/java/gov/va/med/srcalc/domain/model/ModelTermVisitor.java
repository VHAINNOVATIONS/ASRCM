package gov.va.med.srcalc.domain.model;

/**
 * Gang-of-Four Visitor pattern for {@link ModelTerm}s. Lets other classes
 * define new operations on the ModelTerm class hierarchy without modifying it.
 */
public interface ModelTermVisitor
{
    public void visitConstantTerm(final ConstantTerm term);
    
    public void visitDerivedTerm(final DerivedTerm term);
    
    public void visitBooleanTerm(final BooleanTerm term);
    
    public void visitDiscreteTerm(final DiscreteTerm term);
    
    public void visitNumericalTerm(final NumericalTerm term);
    
    public void visitProcedureTerm(final ProcedureTerm term);
}

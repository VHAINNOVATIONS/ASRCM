package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.*;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

/**
 * Orders ModelTerms for user display. The terms are ordered by, in order of precendence:
 * 
 * <ol>
 * <li>Type (Constant, then Rules, then Variables)</li>
 * <li>Display Name</li>
 * <li>For Discrete Variables, the option index.</li>
 * </ol>
 */
public class TermComparator implements Comparator<ModelTerm>
{
    private static int CONSTANT_PRIORITY = 1;
    private static int RULE_PRIORITY = 2;
    private static int VARIABLE_PRIORITY = 3;
    
    private static class OrderingVisitor implements ModelTermVisitor
    {
        // Default values.
        private int fTypePriority = 99;
        private String fDisplayName = "";
        private int fOptionIndex = 0;
        
        /**
         * Convenience factory method to construct an OrderingVisitor and visit the given
         * term.
         */
        public static OrderingVisitor forTerm(final ModelTerm term)
        {
            final OrderingVisitor ov = new OrderingVisitor();
            term.accept(ov);
            return ov;
        }

        @Override
        public void visitConstantTerm(final ConstantTerm term)
        {
            fTypePriority = CONSTANT_PRIORITY;
        }

        @Override
        public void visitDerivedTerm(final DerivedTerm term)
        {
            fTypePriority = RULE_PRIORITY;
            fDisplayName = term.getRule().getDisplayName();
        }
        
        private void visitVariableTerm(final SingleVariableTerm term)
        {
            fTypePriority = VARIABLE_PRIORITY;
            fDisplayName = term.getVariable().getDisplayName();
        }

        @Override
        public void visitBooleanTerm(final BooleanTerm term)
        {
            visitVariableTerm(term);
        }

        @Override
        public void visitDiscreteTerm(final DiscreteTerm term)
        {
            visitVariableTerm(term);
            fOptionIndex = term.getOptionIndex();
        }

        @Override
        public void visitNumericalTerm(final NumericalTerm term)
        {
            visitVariableTerm(term);
        }

        @Override
        public void visitProcedureTerm(final ProcedureTerm term)
        {
            visitVariableTerm(term);
        }

        public int getTypePriority()
        {
            return fTypePriority;
        }

        public String getDisplayName()
        {
            return fDisplayName;
        }

        public int getOptionIndex()
        {
            return fOptionIndex;
        }
    }

    @Override
    public int compare(final ModelTerm a, final ModelTerm b)
    {
        final OrderingVisitor aOrder = OrderingVisitor.forTerm(a);
        final OrderingVisitor bOrder = OrderingVisitor.forTerm(b);
        
        return ComparisonChain.start()
                .compare(aOrder.getTypePriority(), bOrder.getTypePriority())
                .compare(aOrder.getDisplayName(), bOrder.getDisplayName())
                .compare(aOrder.getOptionIndex(), bOrder.getOptionIndex())
                .result();

    }
    
}

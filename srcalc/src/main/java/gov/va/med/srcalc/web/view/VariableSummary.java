package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable.DisplayType;

/**
 * {@link Variable} wrapper for displaying summary information (such as the
 * display name and type name) on a web page.
 */
public class VariableSummary
{
    private final Variable fVariable;
    private final String fTypeName;
    
    protected VariableSummary(final Variable variable)
    {
        fVariable = variable;
        final SummaryVisitor visitor = new SummaryVisitor();
        visitor.visit(variable);
        fTypeName = visitor.getTypeName();
    }
    
    /**
     * Factory method to construct an instance from the given Variable.
     */
    public static VariableSummary fromVariable(final Variable variable)
    {
        return new VariableSummary(variable);
    }
    
    protected Variable getVariable()
    {
        return fVariable;
    }

    public String getDisplayName()
    {
        return fVariable.getDisplayName();
    }

    public String getTypeName()
    {
        return fTypeName;
    }
    
    /**
     * A {@link VariableVisitor} which determines type-specific summary
     * information about a variable.
     */
    private static class SummaryVisitor extends ExceptionlessVariableVisitor
    {
        private String fTypeName;

        public String getTypeName()
        {
            return fTypeName;
        }

        @Override
        public void visitNumerical(final NumericalVariable variable)
        {
            fTypeName = "Numerical";
        }

        @Override
        public void visitBoolean(final BooleanVariable variable)
        {
            fTypeName = "Checkbox";
        }

        @Override
        public void visitMultiSelect(final MultiSelectVariable variable)
        {
            fTypeName = (variable.getDisplayType() == DisplayType.Radio) ?
                    "Radio Button" : "Drop-Down";
        }
        
        @Override
        public void visitLab(final LabVariable variable)
        {
            fTypeName = "Lab Result";
        }

        @Override
        public void visitProcedure(final ProcedureVariable variable)
        {
            fTypeName = "Procedure Selection";
        }
    }
}

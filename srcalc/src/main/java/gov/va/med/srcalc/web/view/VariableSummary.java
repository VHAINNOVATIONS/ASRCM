package gov.va.med.srcalc.web.view;

import com.google.common.base.Joiner;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.model.MultiSelectVariable.DisplayType;

/**
 * {@link Variable} wrapper for displaying summary information (such as the
 * display name and type name) on a web page.
 */
public class VariableSummary
{
    private final Variable fVariable;
    private final String fTypeName;
    private final String fOptionString;
    
    protected VariableSummary(final Variable variable)
    {
        fVariable = variable;
        final SummaryVisitor visitor = new SummaryVisitor();
        visitor.visit(variable);
        fTypeName = visitor.getTypeName();
        fOptionString = visitor.getOptionString();
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
    
    public String getKey()
    {
        return fVariable.getKey();
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
     * Returns a String representing a short description of the variable (i.e. for 
     * {@link NumericalVariable}s the range is displayed)
     */
    public String getOptionString()
    {
        return fOptionString;
    }
    
    /**
     * A {@link VariableVisitor} which determines type-specific summary
     * information about a variable.
     */
    private static class SummaryVisitor extends ExceptionlessVariableVisitor
    {
        private String fTypeName;
        private String fOptionString;

        public String getTypeName()
        {
            return fTypeName;
        }
        
        public String getOptionString()
        {
            return fOptionString;
        }

        @Override
        public void visitNumerical(final NumericalVariable variable)
        {
            fTypeName = "Numerical";
            // Get the range of the numerical
            fOptionString = variable.getValidRange().toString();
        }

        @Override
        public void visitBoolean(final BooleanVariable variable)
        {
            fTypeName = "Checkbox";
            fOptionString = String.format("[%s]", variable.getDisplayName());
        }

        @Override
        public void visitMultiSelect(final MultiSelectVariable variable)
        {
            fTypeName = (variable.getDisplayType() == DisplayType.Radio) ?
                    "Radio Button" : "Drop-Down";
            fOptionString = String.format("[%s]", Joiner.on(", ").join(variable.getOptions()));
        }
        
        @Override
        public void visitDiscreteNumerical(final DiscreteNumericalVariable variable)
        {
            fTypeName = "Discrete Numerical";
            fOptionString = String.format("Range: %s Options: [%s]", 
                    variable.getValidRange().toString(),
                    Joiner.on(", ").join(variable.getOptions()));
        }

        @Override
        public void visitProcedure(final ProcedureVariable variable)
        {
            fTypeName = "Procedure Selection";
            fOptionString = "";
        }
    }
}

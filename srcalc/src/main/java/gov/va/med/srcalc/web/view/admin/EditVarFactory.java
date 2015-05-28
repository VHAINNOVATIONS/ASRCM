package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.ModelInspectionService;

/**
 * Constructs an appropriate instance of {@link EditExistingVar} to edit an
 * existing variable.
 */
public class EditVarFactory
{
    /**
     * Returns an {@link EditExistingVar} instance for editing the given variable.
     * @param variable the target variable
     * @param modelService for EditExistingVar implementations to retrieve any
     * necessary reference data from the Risk Models
     */
    public static EditExistingVar getInstance(
            final AbstractVariable variable,
            final ModelInspectionService modelService)
    {
        return new Visitor(modelService).getInstance(variable);
    }
        
    /**
     * A Visitor to select the right EditVar subclass based on the variable
     * type.
     */
    private static class Visitor extends ExceptionlessVariableVisitor
    {
        private final ModelInspectionService fModelService;
        private EditExistingVar fInstance;
        
        public Visitor(final ModelInspectionService modelService)
        {
            fModelService = modelService;
        }
        
        @Override
        public void visitNumerical(final NumericalVariable variable)
        {
            throw new UnsupportedOperationException(
                    "Editing numerical variables is not yet supported.");
        }

        @Override
        public void visitBoolean(final BooleanVariable variable)
        {
            fInstance = new EditExistingBooleanVar(variable, fModelService);
        }

        @Override
        public void visitMultiSelect(final MultiSelectVariable variable)
        {
            fInstance = new EditExistingMultiSelectVar(variable, fModelService);
        }

        @Override
        public void visitProcedure(final ProcedureVariable variable)
        {
            throw new UnsupportedOperationException(
                    "Editing procedure variables is not yet supported.");
        }

        @Override
        public void visitDiscreteNumerical(DiscreteNumericalVariable variable)
        {
            throw new UnsupportedOperationException(
                    "Editing discrete numerical variables is not yet supported.");
        }
        
        /**
         * Visits the given variable and returns the constructed instance.
         */
        public EditExistingVar getInstance(final AbstractVariable variable)
        {
            visit(variable);
            return fInstance;
        }
        
    }
    
}

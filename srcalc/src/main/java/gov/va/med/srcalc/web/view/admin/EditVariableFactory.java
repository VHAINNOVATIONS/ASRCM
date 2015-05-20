package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.ModelInspectionService;

/**
 * Constructs an appropriate instance of {@link EditExistingVariable} to edit an
 * existing variable.
 */
public class EditVariableFactory
{
    /**
     * Returns an {@link EditExistingVariable} instance for editing the given variable.
     * @param variable the target variable
     * @param modelService for EditExistingVariable implementations to retrieve
     * any necessary reference data from the Risk Models
     */
    public static EditExistingVariable getInstance(
            final AbstractVariable variable,
            final ModelInspectionService modelService)
    {
        return new Visitor(modelService).getInstance(variable);
    }
        
    /**
     * A Visitor to select the right EditVariable subclass based on the variable
     * type.
     */
    private static class Visitor extends ExceptionlessVariableVisitor
    {
        private final ModelInspectionService fModelService;
        private EditExistingVariable fInstance;
        
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
            fInstance = new EditExistingBooleanVariable(
                    variable, fModelService);
        }

        @Override
        public void visitMultiSelect(final MultiSelectVariable variable)
        {
            throw new UnsupportedOperationException(
                    "Editing multi-select variables is not yet supported.");
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
        public EditExistingVariable getInstance(final AbstractVariable variable)
        {
            visit(variable);
            return fInstance;
        }
        
    }
    
}

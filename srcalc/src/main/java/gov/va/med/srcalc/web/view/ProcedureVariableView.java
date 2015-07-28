package gov.va.med.srcalc.web.view;

import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.model.ProcedureVariable;

public class ProcedureVariableView extends VariableView
{
    private static final String PROCEDURE_FRAGMENT = "procedureFragment.jsp";
    
    protected ProcedureVariableView()
    {
        
    }
    
    public ProcedureVariableView(final ProcedureVariable variable, final String referenceInfo)
    {
        super(variable.getDisplayName(), variable.getGroup(), variable.getKey(),
                variable.getHelpText(), Optional.of(referenceInfo), PROCEDURE_FRAGMENT);
    }
}
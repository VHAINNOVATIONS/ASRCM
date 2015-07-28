package gov.va.med.srcalc.web.view;

import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.model.NumericalVariable;

/**
 * This class represents the visible attributes of a {@link NumericalVariable} in order to
 * provide other attributes that are only necessary for display.
 */
public class NumericalVariableView extends VariableView
{
    private static final String NUMERICAL_FRAGMENT = "numericalFragment.jsp";
    
    private final String fUnits;
    
    /**
     * For reflection-based construction only. Business code should use
     * {@link #NumericalVariableView(String, DisplayGroup, String, String, String)}.
     */
    protected NumericalVariableView()
    {
        super();
        fUnits = "";
    }
    
    public NumericalVariableView(final NumericalVariable variable, final String referenceInfo)
    {
        super(variable.getDisplayName(), variable.getGroup(),
                variable.getKey(), variable.getHelpText(), Optional.of(referenceInfo),
                NUMERICAL_FRAGMENT);
        fUnits = variable.getUnits();
    }
    
    public String getUnits()
    {
        return fUnits;
    }
}

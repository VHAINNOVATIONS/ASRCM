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
    
    /**
     * Constructs an instance.
     * @param variable the NumericalVariable to copy properties from
     * @param referenceInfo the reference information for this NumericalVariable
     */
    public NumericalVariableView(final NumericalVariable variable, final String referenceInfo)
    {
        super(variable.getDisplayName(), variable.getGroup(),
                variable.getKey(), variable.getHelpText(), Optional.of(referenceInfo),
                NUMERICAL_FRAGMENT);
        fUnits = variable.getUnits();
    }
    
    /**
     * The units (if any) for the number. May be an empty string if units are
     * not applicable, but will never be null.
     */
    public String getUnits()
    {
        return fUnits;
    }
}

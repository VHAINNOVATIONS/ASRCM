package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.NumericalVariable;

import java.util.List;

/**
 * This class represents the visible attributes of a {@link NumericalVariable} in order to
 * provide other attributes that are only necessary for display.
 */
public class NumericalVariableView extends VariableView
{
    private static final String NUMERICAL_FRAGMENT = "numericalInputs.jsp";
    
    private final String fUnits;
    
    /**
     * Constructs an instance.
     * @param variable the NumericalVariable to copy properties from
     * @param referenceInfo the reference information for this NumericalVariable
     */
    public NumericalVariableView(final NumericalVariable variable, final List<String> referenceInfo)
    {
        super(variable, referenceInfo, NUMERICAL_FRAGMENT);
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

    @Override
    public String getFragmentName()
    {
        return NUMERICAL_FRAGMENT;
    }
}

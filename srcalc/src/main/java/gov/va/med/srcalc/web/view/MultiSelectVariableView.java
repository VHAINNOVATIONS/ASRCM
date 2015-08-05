package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;
import gov.va.med.srcalc.domain.model.MultiSelectVariable.DisplayType;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * This class represents the visible attributes of a {@link MultiSelectVariable} in order to
 * provide other attributes that are only necessary for display.
 */
public class MultiSelectVariableView extends VariableView
{
    private static final String RADIO_FRAGMENT = "radioInputs.jsp";
    private static final String DROPDOWN_FRAGMENT = "dropdownInputs.jsp";
    
    private final DisplayType fDisplayType;
    private final List<MultiSelectOption> fOptions;
    
    /**
     * Constructs an immutable instance. 
     * @param variable the MultiSelectVariable to copy properties from.
     * @param referenceInfo the reference information for this MultiSelectVariable
     */
    public MultiSelectVariableView(final MultiSelectVariable variable, final String referenceInfo)
    {
        super(variable, referenceInfo);
        fDisplayType = variable.getDisplayType();
        fOptions = ImmutableList.copyOf(variable.getOptions());
    }
    
    /**
     * Returns the DisplayType that specifies how this view should be displayed to the user.
     */
    public DisplayType getDisplayType()
    {
        return fDisplayType;
    }
    
    /**
     * Returns the possible options in an ImmutableList.
     */
    public List<MultiSelectOption> getOptions()
    {
        return fOptions;
    }

    @Override
    public String getFragmentName()
    {
        if(fDisplayType.equals(DisplayType.Radio))
        {
            return RADIO_FRAGMENT;
        }
        else
        {
            return DROPDOWN_FRAGMENT;
        }
    }
}

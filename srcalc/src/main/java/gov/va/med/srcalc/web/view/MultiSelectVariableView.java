package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;
import gov.va.med.srcalc.domain.model.MultiSelectVariable.DisplayType;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

/**
 * This class represents the visible attributes of a {@link MultiSelectVariable} in order to
 * provide other attributes that are only necessary for display.
 */
public class MultiSelectVariableView extends VariableView
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiSelectVariableView.class);
    
    private static final String MULTI_SELECT_FRAGMENT = "multiSelectFragment.jsp";
    
    private final DisplayType fDisplayType;
    private final List<MultiSelectOption> fOptions;
    
    /**
     * For reflections-based construction only. Business code should use
     * {@link #MultiSelectVariableView(MultiSelectVariable, String)}.
     */
    MultiSelectVariableView()
    {
        fDisplayType = DisplayType.Radio;
        fOptions = new ArrayList<>();
    }
    
    /**
     * Constructs an immutable instance. 
     * @param variable the MultiSelectVariable to copy properties from.
     * @param referenceInfo the reference information for this MultiSelectVariable
     */
    public MultiSelectVariableView(final MultiSelectVariable variable, final String referenceInfo)
    {
        super(variable.getDisplayName(), variable.getGroup(), variable.getKey(),
                variable.getHelpText(), Optional.of(referenceInfo), MULTI_SELECT_FRAGMENT);
        fDisplayType = variable.getDisplayType();
        fOptions = ImmutableList.copyOf(variable.getOptions());
        LOGGER.debug("Multiselectvariableview {} {} {} {} {}", 
                getDisplayName(), getKey(), getDisplayType(), getOptions(),
                getDisplayGroup());
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
}

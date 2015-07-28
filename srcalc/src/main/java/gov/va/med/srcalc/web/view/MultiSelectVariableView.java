package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.calculation.PopulatedDisplayGroup;
import gov.va.med.srcalc.domain.model.MultiSelectOption;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;
import gov.va.med.srcalc.domain.model.MultiSelectVariable.DisplayType;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

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
    
    public DisplayType getDisplayType()
    {
        return fDisplayType;
    }
    
    public List<MultiSelectOption> getOptions()
    {
        return fOptions;
    }
}

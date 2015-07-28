package gov.va.med.srcalc.web.view;

import com.github.rjeschke.txtmark.Processor;
import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.model.VariableGroup;

/**
 * Contains common properties for items to display to the user.
 */
public abstract class DisplayItem
{
    private final String fDisplayName;
    private final VariableGroup fDisplayGroup;
    private final String fFragmentName;
    private final Optional<String> fHelpText;
    
    protected DisplayItem()
    {
        fFragmentName = "unset fragment";
        fDisplayName = "unset display name";
        fDisplayGroup = new VariableGroup("unset group", 0);
        fHelpText = Optional.absent();
    }
    
    protected DisplayItem(final String displayName, final VariableGroup displayGroup, final String fragmentName,
            final Optional<String> helpText)
    {
        fDisplayName = displayName;
        fDisplayGroup = displayGroup;
        fFragmentName = fragmentName;
        fHelpText = helpText;
    }
    
    public String getDisplayName()
    {
        return fDisplayName;
    }
    
    public VariableGroup getDisplayGroup()
    {
        return fDisplayGroup;
    }
    
    public String getFragmentName()
    {
        return fFragmentName;
    }

    public Optional<String> getHelpText()
    {
        return fHelpText;
    }
    
    public String getHelpTextAsHtml()
    {
        return Processor.process(fHelpText.or(""));
    }
    
    @Override
    public String toString()
    {
        return fDisplayName;
    }
}

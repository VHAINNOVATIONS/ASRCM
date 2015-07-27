package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.VariableGroup;

import com.google.common.base.Optional;

public abstract class VariableView extends DisplayItem
{
    private final String fKey;
    private final Optional<String> fHelpText;
    private final Optional<String> fReferenceInfo;

    /**
     * Constructs an instance with dummy values for the basic properties
     * key, help text, and reference info.
     */
    protected VariableView()
    {
        fKey = "";
        fHelpText = Optional.absent();
        fReferenceInfo = Optional.absent();
    }
    
    protected VariableView(final String displayName, final VariableGroup group,
            final String key, final Optional<String> helpText, final Optional<String> referenceInfo)
    {
        super(displayName, group);
        fKey = key;
        fHelpText = helpText;
        fReferenceInfo = referenceInfo;
    }

    public String getKey()
    {
        return fKey;
    }

    public Optional<String> getHelpText()
    {
        return fHelpText;
    }
    
    public Optional<String> getReferenceInfo()
    {
        return fReferenceInfo;
    }
}

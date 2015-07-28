package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.VariableGroup;

import com.google.common.base.Optional;

public abstract class VariableView extends DisplayItem
{
    private final String fKey;
    
    private final Optional<String> fReferenceInfo;

    /**
     * Constructs an instance with dummy values for the basic properties
     * key, help text, and reference info.
     */
    protected VariableView()
    {
        fKey = "";
        fReferenceInfo = Optional.absent();
    }
    
    protected VariableView(final String displayName, final VariableGroup group,
            final String key, final Optional<String> helpText, final Optional<String> referenceInfo,
            final String displayFragment)
    {
        super(displayName, group, displayFragment, helpText);
        fKey = key;
        fReferenceInfo = referenceInfo;
    }

    public String getKey()
    {
        return fKey;
    }
    
    public Optional<String> getReferenceInfo()
    {
        return fReferenceInfo;
    }
    
    public String getVarPath()
    {
        return VariableEntry.makeDynamicValuePath(fKey);
    }
}

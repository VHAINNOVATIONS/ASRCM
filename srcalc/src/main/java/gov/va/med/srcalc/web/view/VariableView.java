package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.VariableGroup;

import com.google.common.base.Optional;

/**
 * Contains common properties for any Variables to display to the user.
 */
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
    
    /**
     * A constructor that uses properties common to all variables.
     * @param displayName the display name to show to the user
     * @param group the group to which the specified Variable belongs.
     * @param key the unique key for the specified Variable
     * @param helpText the help text for the specified Variable
     * @param referenceInfo the reference information (notes) for the specified Variable
     * @param displayFragment the name of the jsp page used to display this VariableView
     */
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

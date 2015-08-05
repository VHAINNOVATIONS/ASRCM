package gov.va.med.srcalc.web.view;


import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.model.VariableGroup;

/**
 * Contains common properties for items to display to the user.
 */
public interface DisplayItem
{
    /**
     * Returns the display name of this DisplayItem to show to the user.
     */
    public String getDisplayName();
    
    /**
     * Returns the VariableGroup (for display) to which this DisplayItem belongs.
     */
    public VariableGroup getDisplayGroup();
    
    /**
     * Returns the jsp fragment that this DisplayItem will use to build inputs and 
     * display to the user.
     */
    public String getFragmentName();

    /**
     * Returns the help text as an Optional.
     */
    public Optional<String> getHelpText();
    
    /**
     * Transforms the help text from a plain text String to valid HTML, or returns an empty
     * String if the help text is not present.
     */
    public String getHelpTextAsHtml();
    
    /**
     * Returns the reference/description information for this DisplayItem.
     */
    public String getReferenceInfo();
}

package gov.va.med.srcalc.web.view;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.model.VariableGroup;

/**
 * This class represents reference information that will be automatically retrieved from sources
 * such as VistA instead of from the database.
 */
public class ReferenceItem extends DisplayItem
{
    private static final String DISPLAY_FRAGMENT = "referenceItem.jsp";
    
    private final List<String> fReferenceInfo;
    
    protected ReferenceItem()
    {
        fReferenceInfo = Collections.emptyList();
    }
    
    /**
     * Constructs an instance using the designated information.
     * @param displayName the display name of the reference information
     * @param group the VariableGroup that this reference information belongs to
     * @param referenceInfo the reference information to show to the user
     */
    public ReferenceItem(final String displayName, final VariableGroup group, final List<String> referenceInfo)
    {
        super(displayName, group, DISPLAY_FRAGMENT, Optional.of(""));
        fReferenceInfo = referenceInfo;
    }

    /**
     * Returns the reference information in a List so that formatting later is easier.
     * For instance, each List item could be a paragraph element or an unordered item on a page.
     */
    public List<String> getReferenceInfo()
    {
        return fReferenceInfo;
    }
}

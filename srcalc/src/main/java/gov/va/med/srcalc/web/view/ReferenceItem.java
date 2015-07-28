package gov.va.med.srcalc.web.view;

import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.model.VariableGroup;

public class ReferenceItem extends DisplayItem
{
    private static final String DISPLAY_FRAGMENT = "referenceItem.jsp";
    
    private final String fReferenceInfo;
    
    protected ReferenceItem()
    {
        fReferenceInfo = "";
    }
    
    public ReferenceItem(final String displayName, final VariableGroup group, final String referenceInfo)
    {
        super(displayName, group, DISPLAY_FRAGMENT, Optional.of(""));
        fReferenceInfo = referenceInfo;
    }

    public String getReferenceInfo()
    {
        return fReferenceInfo;
    }
}

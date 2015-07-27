package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.VariableGroup;

public class ReferenceItem extends DisplayItem
{
    private final String fReferenceInfo;
    
    protected ReferenceItem()
    {
        fReferenceInfo = "";
    }
    
    public ReferenceItem(final String displayName, final VariableGroup group, final String referenceInfo)
    {
        super(displayName, group);
        fReferenceInfo = referenceInfo;
    }

    public String getReferenceInfo()
    {
        return fReferenceInfo;
    }
}

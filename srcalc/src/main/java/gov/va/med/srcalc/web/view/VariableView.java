package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.model.Variable;
import gov.va.med.srcalc.domain.model.VariableGroup;

import com.github.rjeschke.txtmark.Processor;
import com.google.common.base.Optional;

/**
 * Contains common properties for any Variables to display to the user.
 */
public abstract class VariableView implements DisplayItem
{
    private final Variable fVariable;
    
    private final String fReferenceInfo;
    
    /**
     * A constructor that stores a Variable and supplementary information for user viewing.
     * @param variable the variable to return properties from.
     * @param referenceInfo the reference information (notes) for the specified Variable
     */
    protected VariableView(final Variable variable, final String referenceInfo)
    {
        fVariable = variable;
        fReferenceInfo = referenceInfo;
    }
    
    /**
     * Returns the unique key assigned to the variable that this View contains.
     */
    public String getKey()
    {
        return fVariable.getKey();
    }
    
    @Override
    public String getReferenceInfo()
    {
        return fReferenceInfo;
    }
    
    /**
     * @see VariableEntry#makeDynamicValuePath(String)
     */
    public String getVarPath()
    {
        return VariableEntry.makeDynamicValuePath(fVariable.getKey());
    }
    
    @Override
    public String getDisplayName()
    {
        return fVariable.getDisplayName();
    }

    @Override
    public VariableGroup getDisplayGroup()
    {
        return fVariable.getGroup();
    }
    
    @Override
    public Optional<String> getHelpText()
    {
        return fVariable.getHelpText();
    }

    @Override
    public String getHelpTextAsHtml()
    {
        return Processor.process(fVariable.getHelpText().or(""));
    }
    
    @Override
    public String toString()
    {
        return fVariable.getDisplayName();
    }
}

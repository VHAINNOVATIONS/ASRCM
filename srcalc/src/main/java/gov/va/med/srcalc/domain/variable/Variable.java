package gov.va.med.srcalc.domain.variable;

/**
 * A model variable. Just the variable, does not store an inputted value.
 */
public abstract class Variable
{
    private String fDisplayName;
    private String fHelpText;

    protected Variable()
    {
    }
    
    /**
     * Creates an instance with some of the basic properties filled.
     * @param displayName
     * @param type
     */
    protected Variable(String displayName)
    {
        this.fDisplayName = displayName;
    }
    
    public String getDisplayName()
    {
        return fDisplayName;
    }

    public void setDisplayName(String displayName)
    {
        this.fDisplayName = displayName;
    }

    public String getHelpText()
    {
        return fHelpText;
    }

    public void setHelpText(String helpText)
    {
        this.fHelpText = helpText;
    }
    
    public abstract void accept(VariableVisitor visitor) throws Exception;
}

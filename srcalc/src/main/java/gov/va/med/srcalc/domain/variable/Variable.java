package gov.va.med.srcalc.domain.variable;

/**
 * A model variable. Just the variable, does not store an inputted value.
 */
public abstract class Variable
{
    private String fDisplayName;
    private boolean fRequired;
    private String fHelpText;

    protected Variable()
    {
    }
    
    /**
     * Creates an instance with some of the basic properties filled.
     * @param displayName
     * @param type
     * @param required
     */
    protected Variable(String displayName, boolean required)
    {
        this.fDisplayName = displayName;
        this.fRequired = required;
    }
    
    public String getDisplayName()
    {
        return fDisplayName;
    }

    public void setDisplayName(String displayName)
    {
        this.fDisplayName = displayName;
    }

    public boolean isRequired()
    {
        return fRequired;
    }

    public void setRequired(boolean required)
    {
        this.fRequired = required;
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

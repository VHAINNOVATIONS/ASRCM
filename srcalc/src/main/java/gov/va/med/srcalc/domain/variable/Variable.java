package gov.va.med.srcalc.domain.variable;

/**
 * A model variable. Just the variable, does not store an inputted value.
 */
public interface Variable
{
    public static final int DISPLAY_NAME_MAX = 80;
    
    public String getDisplayName();

    /**
     * Returns the {@link VariableGroup} for this Variable. Never null.
     */
    public VariableGroup getGroup();

    public String getHelpText();
    
    /**
     * Accepts the given {@link VariableVisitor}.
     * @throws Exception if the {@link VariableVisitor} throws an Exception
     */
    public void accept(VariableVisitor visitor) throws Exception;
}

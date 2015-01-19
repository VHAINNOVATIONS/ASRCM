package gov.va.med.srcalc.domain.variable;

/**
 * A model variable. Just the variable, does not store an inputted value.
 */
public interface Variable
{
    public static final int DISPLAY_NAME_MAX = 80;
    
    /**
     * Returns a key which should be unique among all Variables. Ideally should
     * be alphanumeric, though this isn't enforced.
     */
    public String getKey();
    
    /**
     * Returns a String suitable for display to humans. Will be no longer than
     * {@link #DISPLAY_NAME_MAX}.
     */
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

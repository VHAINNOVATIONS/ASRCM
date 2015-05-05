package gov.va.med.srcalc.domain.model;

/**
 * A model variable. Just the variable, does not store an inputted value.
 */
public interface Variable
{
    /**
     * The maximum length of a valid variable display name: {@value}
     */
    public static final int DISPLAY_NAME_MAX = 80;
    
    /**
     * The maximum length of a valid variable key: {@value}
     */
    public static final int KEY_MAX = 40;
    
    /**
     * A regular expression that defines a valid variable key: {@value}
     * @see #getKey()
     */
    public static final String VALID_KEY_REGEX = "\\w+";
   
    /**
     * Returns a key which should be unique among all Variables. The key will
     * match the regular expression {@link #VALID_KEY_REGEX} and be no longer
     * than {@link #KEY_MAX} characters.
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
     * Returns a key that is used to translate vista retrieved values to
     * variable keys. Retrieval keys do not have to be unique.
     */
    public Integer getRetrievalKey();
    
    /**
     * Should not be persisted to the database, as the retrieval date comes from
     * Vista.
     */
    public String getRetrievalDateString();

    public void setRetrievalDateString(String retrievalDateString);
    
    /**
     * Accepts the given {@link VariableVisitor}.
     * @throws Exception if the {@link VariableVisitor} throws an Exception
     */
    public void accept(VariableVisitor visitor) throws Exception;
}

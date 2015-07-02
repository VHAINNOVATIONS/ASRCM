package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.ValueRetriever;

import com.google.common.base.Optional;

/**
 * <p>A model variable. Just the variable, does not store an inputted value.</p>
 * 
 * <p>This interface exists simply to permit subtype interfaces such as
 * {@link DiscreteVariable}. See {@link AbstractVariable} for the canonical
 * implementation of this interface.</p>
 */
public interface Variable
{
    /**
     * English description of the valid key characters for readable error
     * messages.
     * @see #VALID_KEY_REGEX
     */
    public static final String VALID_KEY_CHARACTERS =
            "letters, digits, and underscores";
    
    /**
     * The maximum length of a valid variable key: {@value}
     */
    public static final int KEY_MAX = 40;
    
    /**
     * <p>A regular expression that defines a valid variable key: {@value}</p>
     * 
     * <p>Note that this expression permits a string of any length. Validation
     * code should check the string length first with a length-specific error
     * message.</p>
     * @see #getKey()
     */
    public static final String VALID_KEY_REGEX = "\\w*";
    
    /**
     * The maximum length of a valid help text string: {@value}
     * @see #getHelpText()
     */
    public static final int HELP_TEXT_MAX = 4000;
   
    /**
     * Returns a key which should be unique among all Variables. The key will
     * match the regular expression {@link #VALID_KEY_REGEX} and be no longer
     * than {@link #KEY_MAX} characters.
     */
    public String getKey();
    
    /**
     * Returns a String suitable for display to humans. Will be no longer than
     * {@link DisplayNameConditions#DISPLAY_NAME_MAX} and will only contain {@link
     * DisplayNameConditions#VALID_DISPLAY_NAME_CHARACTERS valid characters}.
     */
    public String getDisplayName();

    /**
     * Returns the {@link VariableGroup} for this Variable. Never null.
     */
    public VariableGroup getGroup();

    /**
     * Returns the variable definition or help text to assist a user in
     * understanding the variable. This is an optional field, so it is
     * represented by an Optional object.
     * @return an Optional which, if present, contains a non-empty String no
     * longer than {@link #HELP_TEXT_MAX} characters. Never null.
     */
    public Optional<String> getHelpText();
    
    /**
     * Returns the RetrievalEnum that is used to translate VistA retrieved
     * values to variable keys. Multiple variables may use the same retriever.
     * 
     * @return possibly null
     */
    public ValueRetriever getRetriever();
    
    /**
     * Accepts the given {@link VariableVisitor}.
     * @throws Exception if the {@link VariableVisitor} throws an Exception
     */
    public void accept(VariableVisitor visitor) throws Exception;
}

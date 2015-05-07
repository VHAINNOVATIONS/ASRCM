package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.util.Preconditions;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.*;

/**
 * Implements base properties for {@link Variable}. Unlike the latter, this
 * class presents a mutable interface.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
// This is the base variable entity so name the table accordingly.
@Table(name = "variable")
public abstract class AbstractVariable implements Variable
{
    /**
     * Precompiled version of {@link Variable#VALID_KEY_REGEX} for efficiency.
     */
    public static final Pattern VALID_KEY_PATTERN  =
            Pattern.compile(VALID_KEY_REGEX);
    
    /**
     * Precompiled version of {@link Variable#VALID_DISPLAY_NAME_REGEX} for
     * efficiency.
     */
    public static final Pattern VALID_DISPLAY_NAME_PATTERN =
            Pattern.compile(Variable.VALID_DISPLAY_NAME_REGEX);

    private int fId;
    private String fDisplayName;
    private VariableGroup fGroup;
    private String fHelpText;
    private String fKey;
    private Integer fRetrievalKey;
    private String fRetrievalDateString;

    /**
     * Constructs an instance with dummy values for the basic properties
     * displayName and group.
     */
    protected AbstractVariable()
    {
    	fKey = "unset";
        fDisplayName = "unset";
        fGroup = new VariableGroup("unset group", 0);
        fRetrievalDateString = "";
    }
    
    /**
     * Creates an instance with some of the basic properties filled.
     * @throws NullPointerException if any of the given arguments is null.
     * @throws IllegalArgumentException if a given value is invalid. (See the
     * respective setters.)
     * @see #setKey(String)
     * @see #setDisplayName(String)
     * @see #setGroup(VariableGroup)
     */
    protected AbstractVariable(final String displayName, final VariableGroup group, final String key)
    {
        // Use the setters to enforce contraints.
        setKey(key);
        setDisplayName(displayName);
        setGroup(group);
        this.fRetrievalDateString = "";
    }
    
    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id
    public final int getId()
    {
        return fId;
    }

    /**
     * For reflection-based construction only. Business code should never modify
     * the surrogate key as it is generated from the database.
     */
    final void setId(final int id)
    {
        this.fId = id;
    }
    
    @Basic
    @Column(
                // override the default name "KEY" which is a reserved word
    		name = "VARIABLE_KEY",
    		length = KEY_MAX,
    		nullable = false,
    		unique = true)
    public final String getKey()
    {
        return fKey;
    }
    
    /**
     * Sets the internal key of the variable. Since the key should not be
     * updated on an existing variable, this method is for bean construction
     * only.
     * @throws IllegalArgumentException if the given key is empty, over 40
     * characters, or does not match {@link Variable#VALID_KEY_REGEX}
     */
    final void setKey(final String key)
    {
        // Check preconditions
        Preconditions.requireWithin(key, 1, KEY_MAX); // require at least 1 char
        Preconditions.requireMatches(key, "key", VALID_KEY_PATTERN);
        
        fKey = key;
    }
    
    @Basic
    @Column(
            length = DISPLAY_NAME_MAX,
            nullable = false)
    public final String getDisplayName()
    {
        return fDisplayName;
    }

    /**
     * Sets the name of the variable for display to the user.
     * @throws IllegalArgumentException if the given name is empty, over
     * {@link Variable#DISPLAY_NAME_MAX} characters, or does not match
     * {@link Variable#VALID_DISPLAY_NAME_REGEX}
     */
    public final void setDisplayName(final String displayName)
    {
        // require at least 1 character
        Preconditions.requireWithin(displayName, 1, DISPLAY_NAME_MAX);
        Preconditions.requireMatches(displayName, "displayName", VALID_DISPLAY_NAME_PATTERN);
        fDisplayName = displayName;
    }

    /**
     * Returns the {@link VariableGroup} for this Variable. Never null.
     */
    @ManyToOne
    @JoinColumn(name = "VARIABLE_GROUP", nullable = false)  // "group" is a SQL reserved word
    public final VariableGroup getGroup()
    {
        return fGroup;
    }

    /**
     * Sets the {@link VariableGroup} for this Variable.
     * @param group must not be null
     * @throws NullPointerException if the given group is null
     */
    public final void setGroup(final VariableGroup group)
    {
        fGroup = Objects.requireNonNull(group, "group must not be null");
    }

    @Basic
    public final String getHelpText()
    {
        return fHelpText;
    }

    public final void setHelpText(final String helpText)
    {
        this.fHelpText = helpText;
    }
    
    @Basic
    @Column(
            length = KEY_MAX,
            nullable = true)
    public final Integer getRetrievalKey()
    {
    	return fRetrievalKey;
    }
    
    public final void setRetrievalKey(final Integer retrievalKey)
    {
    	this.fRetrievalKey = retrievalKey;
    }
    
    @Transient
    public String getRetrievalDateString()
    {
		return fRetrievalDateString;
	}

    /**
     * 
     * @param retrievalDateString The properly formatted string representing the date
     * 		when this variable was automatically retrieved.
     */
	public void setRetrievalDateString(final String retrievalDateString)
	{
		this.fRetrievalDateString = retrievalDateString;
	}

	@Override
    public String toString()
    {
        return getDisplayName();
    }
	
	/**
	 * Considers two {@link AbstractVariable} are equal if, and only if their keys are equal.
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof AbstractVariable)
		{
			return Objects.equals(this.fKey, ((AbstractVariable) other).getKey());
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Returns a hash code based on the variable key that uses the String.hashCode() implementation.
	 */
	@Override
	public int hashCode()
	{
		return this.fKey.hashCode();
	}
}

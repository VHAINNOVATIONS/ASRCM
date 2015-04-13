package gov.va.med.srcalc.domain.variable;

import gov.va.med.srcalc.util.Preconditions;

import java.util.Objects;

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
     */
    protected AbstractVariable(final String displayName, final VariableGroup group, final String key)
    {
        this.fDisplayName = displayName;
        this.fGroup = Objects.requireNonNull(group, "group must not be null");
        this.fKey = key;
        this.fRetrievalDateString = "";
    }
    
    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id
    public int getId()
    {
        return fId;
    }

    /**
     * For reflection-based construction only. Business code should never modify
     * the surrogate key as it is generated from the database.
     */
    void setId(final int id)
    {
        this.fId = id;
    }
    
    /**
     * The default column name would be "KEY", which is already a 
     * reserved word in SQL. Because of this, the column name is specified
     * rather than using the default.
     */
    @Basic
    @Column(
    		name = "VARIABLE_KEY",
    		length = KEY_MAX,
    		nullable = false,
    		unique = true)
    public String getKey()
    {
        return fKey;
    }
    
    /**
     * Sets the internal key of the variable
     * @throws IllegalArgumentException if the given key is over 40 characters
     */
    public void setKey(final String key)
    {
    	this.fKey = Preconditions.requireWithin(key, KEY_MAX);
    }
    
    @Basic
    @Column(
            length = DISPLAY_NAME_MAX,
            nullable = false)
    public String getDisplayName()
    {
        return fDisplayName;
    }

    /**
     * Sets the name of the variable for display to the user.
     * @throws IllegalArgumentException if the given name is over 80 characters
     */
    public void setDisplayName(final String displayName)
    {
        this.fDisplayName = Preconditions.requireWithin(displayName, DISPLAY_NAME_MAX);
    }

    /**
     * Returns the {@link VariableGroup} for this Variable. Never null.
     */
    @ManyToOne
    @JoinColumn(name = "VARIABLE_GROUP", nullable = false)  // "group" is a SQL reserved word
    public VariableGroup getGroup()
    {
        return fGroup;
    }

    /**
     * Sets the {@link VariableGroup} for this Variable.
     * @param group must not be null
     * @throws NullPointerException if the given group is null
     */
    public void setGroup(final VariableGroup group)
    {
        fGroup = Objects.requireNonNull(group, "group must not be null");
    }

    @Basic
    public String getHelpText()
    {
        return fHelpText;
    }

    public void setHelpText(final String helpText)
    {
        this.fHelpText = helpText;
    }
    
    @Basic
    @Column(
            length = KEY_MAX,
            nullable = true)
    public Integer getRetrievalKey()
    {
    	return fRetrievalKey;
    }
    
    public void setRetrievalKey(final Integer retrievalKey)
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
	 * Keys are unique, so two {@link AbstractVariable} are equal if their keys are equal.
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
	 * Uses the String.hashCode() implementation.
	 */
	@Override
	public int hashCode()
	{
		return this.fKey.hashCode();
	}
}

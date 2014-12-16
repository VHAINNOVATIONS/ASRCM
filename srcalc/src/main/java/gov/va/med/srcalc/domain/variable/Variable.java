package gov.va.med.srcalc.domain.variable;

import java.util.Objects;

import javax.persistence.*;

/**
 * A model variable. Just the variable, does not store an inputted value.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Variable
{
    public static final int DISPLAY_NAME_MAX = 80;
    
    private int fId;
    private String fDisplayName;
    private VariableGroup fGroup;
    private String fHelpText;

    /**
     * Constructs an instance with dummy values for the basic properties
     * displayName and group.
     */
    protected Variable()
    {
        fDisplayName = "unset";
        fGroup = new VariableGroup("unset group", 0);
    }
    
    /**
     * Creates an instance with some of the basic properties filled.
     */
    protected Variable(final String displayName, final VariableGroup group)
    {
        this.fDisplayName = displayName;
        this.fGroup = Objects.requireNonNull(group, "group must not be null");
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
     * Returns the name of the variable for display to the user. Capped at 80
     * characters.
     * @see #DISPLAY_NAME_MAX
     */
    @Basic
    @Column(
            length = DISPLAY_NAME_MAX,
            nullable = false,
            unique = true)   // for now, we use display name as a key, so don't allow dupes
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
        if (displayName.length() > DISPLAY_NAME_MAX)
        {
            throw new IllegalArgumentException(
                    "The display name must be 80 characters or less.");
        }
        this.fDisplayName = displayName;
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
    
    @Override
    public String toString()
    {
        return getDisplayName();
    }
    
    /**
     * Accepts the given {@link VariableVisitor}.
     * @throws Exception if the {@link VariableVisitor} throws an Exception
     */
    public abstract void accept(VariableVisitor visitor) throws Exception;
}

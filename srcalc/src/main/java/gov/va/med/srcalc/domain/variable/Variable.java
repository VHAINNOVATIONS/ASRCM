package gov.va.med.srcalc.domain.variable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * A model variable. Just the variable, does not store an inputted value.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Variable
{
    private int fId;
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
    protected Variable(final String displayName)
    {
        this.fDisplayName = displayName;
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
    
    @Basic
    public String getDisplayName()
    {
        return fDisplayName;
    }

    public void setDisplayName(final String displayName)
    {
        this.fDisplayName = displayName;
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

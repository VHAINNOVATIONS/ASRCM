package gov.va.med.srcalc.domain.model;

import java.util.Objects;

import javax.persistence.*;

/**
 * <p>Represents a group of {@link Variable}s, usually for display grouping.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
public final class VariableGroup implements Comparable<VariableGroup>
{
    /**
     * The well-known name of the procedure group. This group name cannot
     * be changed in the administrator UI.
     */
    public static final String PROCEDURE_GROUP = "Planned Procedure";
    /**
     * The well-known name of the clinical group. This group name cannot
     * be changed in the administrator UI.
     */
    public static final String CLINICAL_GROUP = "Clinical Conditions or Diseases - Recent";
    /**
     * The well-known name of the medications group. This group name cannot
     * be changed in the administrator UI.
     */
    public static final String MEDICATIONS_GROUP = "Medications";
    
    private int fId;
    private String fName;
    private int fDisplayOrder;
    
    /**
     * For reflection-based construction only. A {@link VariableGroup} without
     * a name is degenerate, so use {@link #VariableGroup(String, int)} for normal
     * construction.
     */
    VariableGroup()
    {
        fName = "Programming error! No name!";
    }
    
    /**
     * Constructs an instance.
     * @param name
     * @param displayOrder
     */
    public VariableGroup(final String name, final int displayOrder)
    {
        fName = name;
        fDisplayOrder = displayOrder;
    }

    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id // We use method-based property detection throughout the app.
    @GeneratedValue
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
     * Returns the group's name, never null.
     */
    @Basic
    public String getName()
    {
        return fName;
    }

    /**
     * Sets the name.
     * @param name must not be null
     * @throws NullPointerException if the given name is null
     */
    public void setName(final String name)
    {
        fName = Objects.requireNonNull(name, "name must not be null");
    }

    /**
     * Returns the group's display order, 0-based.
     */
    @Basic
    public int getDisplayOrder()
    {
        return fDisplayOrder;
    }

    /**
     * Sets the group's display order, 0-based. Be careful when setting the
     * order so that you don't end up with two groups with the same order value.
     */
    public void setDisplayOrder(final int displayOrder)
    {
        fDisplayOrder = displayOrder;
    }
    
    /**
     * Returns true if this {@link VariableGroup} is equal to another based
     * on business properties (as opposed to the database ID).
     */
    @Override
    public boolean equals(Object obj)
    {
        // Performance optimization since groups may often be compared.
        if (obj == this)
        {
            return true;
        }
        
        if (obj instanceof VariableGroup)
        {
            VariableGroup other = (VariableGroup)obj;
            
            return Objects.equals(this.getName(), other.getName()) &&
                    this.getDisplayOrder() == other.getDisplayOrder();
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getName(), getDisplayOrder());
    }

    @Override
    public int compareTo(final VariableGroup other)
    {
        // First compare the display order
        final int orderComparison = Integer.compare(
                this.getDisplayOrder(), other.getDisplayOrder());
        if (orderComparison != 0)
        {
            return orderComparison;
        }
        
        // If the display order is equal (which is bad), compare the strings
        // to be consistent with equals().
        return this.getName().compareTo(other.getName());
    }
    
    /**
     * Returns the name.
     */
    @Override
    public String toString()
    {
        return getName();
    }
}

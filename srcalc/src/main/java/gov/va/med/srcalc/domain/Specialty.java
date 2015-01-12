package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.variable.*;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

/**
 * <p>Represents a surgical specialty, with the associated calcuation variables.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
public final class Specialty implements Serializable
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    private int fId;
    
    private int fVistaId;
    
    private String fName;
    
    private List<AbstractVariable> fVariables = new ArrayList<>();

    public Specialty()
    {
    }
    
    public Specialty(final int vistaId, final String name)
    {
        this.fVistaId = vistaId;
        this.fName = name;
    }

    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id // We use method-based property detection throughout the app.
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
     * The specialty's national VistA specialty ID (SURGICAL SPECIALTY file). Note
     * that two SRCalc specialties (e.g., General and Other) may have the same
     * VistA ID.
     */
    @Basic
    public int getVistaId()
    {
        return fVistaId;
    }

    /**
     * For reflection-based construction only. The application assumes that the
     * VistA ID does not change and therefore uses this field for value equality.
     */
    void setVistaId(final int vistaId)
    {
        this.fVistaId = vistaId;
    }

    @Basic
    public String getName()
    {
	return fName;
    }

    public void setName(final String name)
    {
	fName = name;
    }
    
    /**
     * Returns all {@link AbstractVariable}s associated with this Specialty. Caution:
     * lazy-loaded.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @OrderColumn(name = "display_order")
    // Override strange defaults. See
    // <https://forum.hibernate.org/viewtopic.php?f=1&t=1037190>.
    @JoinTable(
            name = "specialty_variable",
            joinColumns = @JoinColumn(name = "specialty_id"),
            inverseJoinColumns = @JoinColumn(name = "variable_id")
        )
    public List<AbstractVariable> getVariables()
    {
        return fVariables;
    }
    
    /**
     * Builds a brand-new, sorted list of {@link PopulatedVariableGroup}s from
     * the {@link Specialty}'s variables.
     */
    protected final List<PopulatedVariableGroup> buildVariableGroupList()
    {
        // Bucket the Variables according to VariableGroup.
        final HashMap<VariableGroup, List<Variable>> map = new HashMap<>();
        for (final AbstractVariable var : getVariables())
        {
            final VariableGroup group = var.getGroup();
            if (!map.containsKey(group))
            {
                final ArrayList<Variable> varList = new ArrayList<>();
                map.put(group, varList);
            }
            map.get(group).add(var);
        }
        
        // Transform the map into PopulatedVariableGroups.
        final ArrayList<PopulatedVariableGroup> groupList =
                new ArrayList<>(map.values().size());
        for (final List<Variable> varList : map.values())
        {
            groupList.add(new PopulatedVariableGroup(varList));
        }
        
        // Finally, sort the List.
        Collections.sort(groupList);
        
        return groupList;
    }
    
    /**
     * Returns all {@link AbstractVariable}s associated with this Specialty, bucketed
     * into their groups.
     * @return an immutable List, sorted in group order
     */
    @Transient
    public List<PopulatedVariableGroup> getVariableGroups()
    {
        // Construct a new instance every time for now. May cache the list if
        // this becomes a performance issue.
        return Collections.unmodifiableList(buildVariableGroupList());
    }
    
    /**
     * For reflection-based construction only. The collection should be modified
     * via {@link #getVariables()}.
     */
    void setVariables(final List<AbstractVariable> variables)
    {
        fVariables = variables;
    }
    
    @Override
    public String toString()
    {
        return fName;
    }
    
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof Specialty)  // false if o == null
        {
            final Specialty other = (Specialty)o;
            
            // Compare the VistA ID and name as this pair should always be
            // unique.
            return (this.getVistaId() == other.getVistaId()) &&
                   Objects.equals(this.getName(), (other.getName()));
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getVistaId(), getName());
    }
}


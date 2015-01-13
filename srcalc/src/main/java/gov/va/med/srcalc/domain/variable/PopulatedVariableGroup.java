package gov.va.med.srcalc.domain.variable;

import java.util.*;

/**
 * <p>Contains a list of {@link Variable}s in the same {@link VariableGroup}.
 * This is a separate class from VariableGroup because the latter is a
 * persistent entity.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class PopulatedVariableGroup implements Comparable<PopulatedVariableGroup>
{
    private final List<Variable> fVariables;
    
    /**
     * Constructs an instance from the given List of variables.
     * @param variables must have at least one member
     * @throws IllegalArgumentException if the given list is empty or if it
     * contains Variables with different groups
     */
    public PopulatedVariableGroup(final List<? extends Variable> variables)
    {
        // Check precondition: variable list is not empty
        if (variables.isEmpty())
        {
            throw new IllegalArgumentException("variable list must not be empty");
        }
        
        // Check precondition: variables all have the same group
        final VariableGroup group = variables.get(0).getGroup();
        for (final Variable var : variables)
        {
            if (!var.getGroup().equals(group))
            {
                throw new IllegalArgumentException(
                        "variables do not all have the same group");
            }
        }
        
        // Preconditions satisfied: store a defensive copy of the list.
        fVariables = new ArrayList<>(variables);
    }

    /**
     * <p>Returns the List of Variables, always containing at least 1 Variable. All
     * contained Variables will have equal {@link VariableGroup}s. The
     * collection itself is immutable, though the variables themselves are not.</p>
     * 
     * <p>Order is preserved from construction.</p>
     */
    public List<Variable> getVariables()
    {
        return Collections.unmodifiableList(fVariables);
    }
    
    public VariableGroup getGroup()
    {
        // Since all Variables have the same group, just use the first's group.
        return fVariables.get(0).getGroup();
    }
    
    /**
     * Returns the VariableGroup's name.
     */
    public String getName()
    {
        return getGroup().getName();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof PopulatedVariableGroup)
        {
            final PopulatedVariableGroup other = (PopulatedVariableGroup)obj;
            return this.getVariables().equals(other.getVariables());
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return getVariables().hashCode();
    }
    
    /**
     * <p>Compares this {@link PopulatedVariableGroup} with another. Comparison
     * is based only on each object's associated {@link VariableGroup}.</p>
     * 
     * <p>Not consistent with equals: two {@link PopulatedVariableGroup}s with
     * different sets of variables from the same group are non-equal but will
     * compare as equal.</p>
     */
    @Override
    public int compareTo(PopulatedVariableGroup o)
    {
        return this.getGroup().compareTo(o.getGroup());
    }
    
    @Override
    public String toString()
    {
        return String.format(
                "Variable Group '%s' with variables %s",
                getGroup(),
                getVariables());
    }
}

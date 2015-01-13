package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

/**
 * <p>A ModelTerm related to a particular option of a {@link DiscreteVariable}.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
public final class DiscreteTerm extends ModelTerm
{
    private static final int UNSET_INDEX = -1;

    private DiscreteVariable fVariable = null;
    private int fOptionIndex = UNSET_INDEX;
    private MultiSelectOption fOption = null;
    
    DiscreteTerm()
    {
    }

    /**
     * Constructs an instance.
     * @param variable
     * @param optionIndex
     * @param coefficient
     * @throws IndexOutOfBoundsException if the index does not exist in the
     * variable's options
     */
    public DiscreteTerm(
            final DiscreteVariable variable, final int optionIndex, final float coefficient)
    {
        super(coefficient);
        fVariable = Objects.requireNonNull(variable);
        fOptionIndex = optionIndex;
        setOption();
    }
    
    /**
     * @throws IndexOutOfBoundsException if the index does not exist in the
     * variable's options
     */
    private void setOption()
    {
        fOption = fVariable.getOptions().get(fOptionIndex);
    }
    
    @ManyToOne(optional = false, targetEntity = AbstractVariable.class)
    public DiscreteVariable getVariable()
    {
        return fVariable;
    }

    /**
     * For reflection-based construction only. The associated Variable should
     * not be changed.
     */
    void setVariable(final DiscreteVariable variable)
    {
        fVariable = Objects.requireNonNull(variable);
        
        // The option index may not have been set yet. Initialize the option if
        // it has been.
        if (fOptionIndex != UNSET_INDEX)
        {
            setOption();
        }
    }

    @Basic
    public int getOptionIndex()
    {
        return fOptionIndex;
    }

    /**
     * For reflection-based construction only. The associated option should not
     * be changed.
     */
    void setOptionIndex(final int optionIndex)
    {
        fOptionIndex = optionIndex;
        
        // The variable may not have been set yet. Initialize the option if it
        // has been.
        if (fVariable != null)
        {
            setOption();
        }
    }
    
    /**
     * Returns the associated {@link MultiSelectOption}.
     */
    @Transient
    public MultiSelectOption getOption()
    {
        return fOption;
    }

    @Override
    @Transient
    public Set<Variable> getRequiredVariables()
    {
        return CollectionUtils.<Variable>unmodifiableSet(fVariable);
    }
    
    @Override
    public boolean equals(final Object o)
    {
        // Performance optimization.
        if (o == this)
        {
            return true;
        }
        
        if (o instanceof DiscreteTerm)
        {
            final DiscreteTerm other = (DiscreteTerm)o;
            return
                    baseEquals(other) &&
                    // Variable is non-null.
                    this.getVariable().equals(other.getVariable()) &&
                    this.getOptionIndex() == other.getOptionIndex();
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getCoefficient(), getVariable(), getOptionIndex());
    }
}
